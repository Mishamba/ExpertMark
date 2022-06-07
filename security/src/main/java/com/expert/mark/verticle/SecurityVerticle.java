package com.expert.mark.verticle;

import com.expert.mark.model.account.User;
import com.expert.mark.repository.UserRepository;
import com.expert.mark.repository.impl.UserRepositoryImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.HashSet;
import java.util.Set;

public class SecurityVerticle extends AbstractVerticle {
    private final JWTAuth jwtAuth = JWTAuth.create(Vertx.vertx(), this.getConfig());
    private final UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public void start() {
        Router router = Router.router(vertx);
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.PUT);
        allowMethods.add(HttpMethod.OPTIONS);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);

        router.route().handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));

        // TODO add role and username to send
        router.route().handler(BodyHandler.create());
        router.post("/authorize").handler(ctx -> {
            String jwt = authorize(ctx.getBodyAsJson());
            ctx.response().setStatusCode(jwt.equals("notAuthorized") ? 403 : 200).send(jwt);
        });
        vertx.createHttpServer().requestHandler(router).listen(8085).onFailure(Throwable::printStackTrace);

        vertx.eventBus().consumer("authenticate", message ->
                jwtAuth.authenticate(new JsonObject().put("token", message.body()))
                .onSuccess(user -> message.reply(new JsonObject()
                        .put("username", user.attributes().getJsonObject("accessToken").getString("username"))
                        .put("role", user.attributes().getJsonObject("accessToken").getString("role"))))
                .onFailure(res -> {
                    res.printStackTrace();
                    message.reply(res.getMessage());
                })
        );
    }

    private JWTAuthOptions getConfig() {
        return new JWTAuthOptions()
                .setKeyStore(new KeyStoreOptions()
                        .setPath("/home/mishamba/java/ExpertMark/security/src/main/resources/keystore.jceks")
                        .setPassword("secret"));
    }

    private String authorize(JsonObject credentials) {
        User user = userRepository.getUserByUsernameWithoutProfile(credentials.getString("username"));
        if (user.getPassword().equals(credentials.getString("password"))) {
            return jwtAuth.generateToken(new JsonObject()
                    .put("username", user.getUsername())
                    .put("role", user.getRole().name()));
        } else {
            return "notAuthorized";
        }
    }
}
