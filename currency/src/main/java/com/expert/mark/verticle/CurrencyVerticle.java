package com.expert.mark.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class CurrencyVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        Router router = Router.router(Vertx.vertx());
        router.get("/currency/:asset").handler(this::getAssetCurrency);

        vertx.createHttpServer().requestHandler(router).listen(8088);
    }

    private void getAssetCurrency(RoutingContext routingContext) {
        String assetName = routingContext.pathParam("asset");
        Double price = null;
        switch (assetName) {
            case "BTC":
                price = 40000D;
                break;
            case "ETH":
                price = 2000D;
                break;
            case "Google":
                price = 2500D;
                break;
        }
        routingContext.response().setStatusCode((price == null) ? 500 : 200).
                putHeader("Content-Type", "application/json").
                send(new JsonObject().put("price", price).encode());
    }
}
