package com.expert.mark.service.impl;

import com.expert.mark.model.account.Character;
import com.expert.mark.model.account.expert.ExpertStatistic;
import com.expert.mark.model.content.forecast.Forecast;
import com.expert.mark.repository.ExpertStatisticRepository;
import com.expert.mark.repository.ForecastRepository;
import com.expert.mark.repository.impl.ExpertStatisticRepositoryImpl;
import com.expert.mark.repository.impl.ForecastRepositoryImpl;
import com.expert.mark.service.ForecastProcessor;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

import java.util.Date;
import java.util.List;

public class ForecastProcessorImpl implements ForecastProcessor {
    private final ForecastRepository forecastRepository = new ForecastRepositoryImpl();
    private final ExpertStatisticRepository expertStatisticRepository = new ExpertStatisticRepositoryImpl();
    private final WebClient webClient = WebClient.create(Vertx.vertx());

    @Override
    public void updateExpertStatisticsAndCalculateForecastAccuracy() {
        for (ExpertStatistic expertStatistic : expertStatisticRepository.getUpdateRequiredExpertStatistics()) {
            String username = expertStatistic.getExpertUsername();
            List<Forecast> forecasts = forecastRepository.findUsersForecasts(username);
            forecasts.forEach(this::processForecastAccuracyCalculation);

            //sort by create date
            forecasts.sort((forecast, t1) ->
                    forecast.getCreateDate().after(t1.getCreateDate()) ? 1 :
                            forecast.getCreateDate().equals(t1.getCreateDate()) ? 0 : -1);

            Float expertAccuracy = 0F;
            int quantity = 0;
            Date lastForecast = null;
            long creationInterval = 0;

            for (Forecast forecast : forecasts) {
                if (lastForecast == null) {
                    lastForecast = forecast.getCreateDate();
                } else {
                    creationInterval += Math.abs(lastForecast.getTime() - forecast.getCreateDate().getTime());
                }
                quantity++;
                expertAccuracy+=forecast.getAccuracy();
            }

            expertAccuracy/=quantity;
            creationInterval/=quantity;

            Character expertCharacter = Character.provideCharacter(expertAccuracy, creationInterval);

            expertStatistic.setAccuracy(expertAccuracy);
            expertStatistic.setCharacter(expertCharacter);
            expertStatistic.setLastUpdateDate(new Date());
            expertStatisticRepository.updateExpertStatistic(expertStatistic);
        }
    }

    private void processForecastAccuracyCalculation(Forecast forecast) {
        String assetName = forecast.getAssetName();
        webClient.get("https://api.nomics.com/v1/currencies/ticker?key=ff714c134e6e9e935f88d4b74c3b2c81839cbf1d&ids=" +
                        assetName +
                        "&interval=1d,30d&convert=USD&platform-currency=ETH&per-page=100&page=1").
                send().
                onComplete(res -> {
                    double actualPrice = res.result().bodyAsJsonObject().getDouble("price");
                    double predictedPrice = forecast.getMethodData().getResult();
                    Float accuracy = 100 - (float) ((Math.abs(predictedPrice - actualPrice) / actualPrice) * 100);
                    forecast.setAccuracy(accuracy);
                });
    }
}
