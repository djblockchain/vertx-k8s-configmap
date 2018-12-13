package org.mjad.util.k8s.vertxcfgmap;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;


public class MainVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void start(Future<Void> fut) {


        ConfigStoreOptions store = new ConfigStoreOptions()
                .setType("configmap")
                .setOptional(false)
                .setConfig(new JsonObject()
                        .put("namespace", "myk8snamespace")
                        .put("name", "my-json-configmap")
                        .put("key", "config.json")
                );

        ConfigRetriever retriever = ConfigRetriever.create(vertx,
                new ConfigRetrieverOptions()
                        .addStore(store));

        retriever.getConfig(conf -> {
            if (conf.failed()) {
                logger.info("failed to get config");
            }

            //if (conf.succeeded() && !conf.result().isEmpty()) {
            if (conf.succeeded()) {
                // Set new loaded config
                config().mergeIn(conf.result());

                // Output config
                logger.info(config().toString());

                Router router = Router.router(vertx);
                router.route("/ping").handler(routingContext -> {
                    HttpServerResponse response = routingContext.response();
                    response
                            .putHeader("content-type", "text/html")
                            .end("<h1>PONG !</h1>");
                });

                vertx.createHttpServer()
                    .requestHandler(router::accept)
                    .listen(config().getInteger("http-port", 80));

            } else {
                logger.fatal("No config detected, closing verticle");
                vertx.close();
            }
        });

    }


}
