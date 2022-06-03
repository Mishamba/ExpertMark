package com.expert.mark.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class CurrencyVerticle extends AbstractVerticle {
    private Logger logger = LoggerFactory.getLogger(CurrencyVerticle.class.getName());
    private AtomicInteger BTCPrice = new AtomicInteger(29000);
    private AtomicInteger ETHPrice = new AtomicInteger(2000);
    private AtomicInteger DogeCoinPrice = new AtomicInteger(200);

    @Override
    public void start() throws Exception {
        Router router = Router.router(Vertx.vertx());
        router.route().handler(ctx -> {
            logger.debug("request to url {}", ctx.request().uri());
        });
        router.get("/currency/:asset").handler(this::getAssetCurrency);

        vertx.createHttpServer().requestHandler(router).listen(8088);
        vertx.setPeriodic(1000, this::changePrice);
    }

    private void changePrice(Long aLong) {
        double random = Math.random();
        double multiplayer = (random * 1000 % 2 == 0) ? random : 1 + (1 - random);
        BTCPrice.set((int) (BTCPrice.get() * multiplayer));
        ETHPrice.set((int) (ETHPrice.get() * multiplayer));
        DogeCoinPrice.set((int) (ETHPrice.get() * multiplayer));
    }

    private void getAssetCurrency(RoutingContext routingContext) {
        String assetName = routingContext.pathParam("asset");
        Integer price = null;
        switch (assetName) {
            case "BTC":
                price = BTCPrice.get();
                break;
            case "ETH":
                price = ETHPrice.get();
                break;
            case "Doge":
                price = DogeCoinPrice.get();
                break;
        }
        routingContext.response().setStatusCode((price == null) ? 500 : 200).
                putHeader("Content-Type", "application/json").
                send(new JsonObject().put("price", price).encode());
    }
}
