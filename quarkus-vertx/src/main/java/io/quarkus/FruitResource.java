package io.quarkus;

import io.quarkus.vertx.web.Route;
import io.reactiverse.reactivex.pgclient.PgPool;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import java.util.NoSuchElementException;

import static io.vertx.core.http.HttpMethod.DELETE;
import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;
import static io.vertx.core.http.HttpMethod.PUT;

/**
 * @author Heiko Braun
 * @since 17/01/2017
 */

@ApplicationScoped
public class FruitResource {

    @Inject
    public PgPool pool;


    private final Jsonb jsonb;

    public FruitResource() {
        jsonb = JsonbBuilder.create(new JsonbConfig());
    }

    @PostConstruct
    public void init() {
/*
        pool.rxQuery("DROP TABLE IF EXISTS known_fruits")
                .blockingGet();
        pool.rxQuery("DROP SEQUENCE IF EXISTS known_fruits_id_seq")
                .blockingGet();

        pool.rxQuery("CREATE TABLE known_fruits (id integer not null, name varchar(40) unique, PRIMARY KEY (id))")
                .blockingGet();
        pool.rxQuery("CREATE SEQUENCE known_fruits_id_seq")
                .blockingGet();

        pool.rxPreparedBatch("INSERT INTO known_fruits (id, name) VALUES (nextval('known_fruits_id_seq'), $1)",
                Arrays.asList(Tuple.of("Cherry"), Tuple.of("Apple"), Tuple.of("Banana")))
                .blockingGet();
*/
    }

    void addStaticHandler(@Observes Router router) {
        router.get("/").handler(StaticHandler.create());
    }


    @Route(path = "/fruits", methods = GET, produces = "application/json")
    public void get(RoutingContext context) {
        StringBuilder response = new StringBuilder();
        response.append("[");
        Fruit.findAll(pool).subscribe(
                fruit -> response.append(jsonb.toJson(fruit).concat(",")),
                e -> context.response().setStatusCode(500).end(e.getLocalizedMessage()),
                () -> {
                    response.append("]");
                    context.response().end(response.toString());
                }
        );
    }

    @Route(path = "/fruits/:id", methods = GET, produces = "application/json")
    public void getSingle(RoutingContext context) {

        Integer id = Integer.valueOf(context.request().getParam("id"));

        Fruit.findById(pool, id).subscribe(
                fruit -> context.response().write(jsonb.toJson(fruit)).setStatusCode(200).end(),
                e -> {
                    if (e instanceof NoSuchElementException)
                        context.response().setStatusCode(404).end("Fruit with id of " + id + " does not exist.");
                    context.response().setStatusCode(500).end(e.getMessage());
                });
    }

    @Route(path = "/fruits", methods = POST, consumes = "application/json", produces = "application/json")
    public void create(RoutingContext context) {
        Fruit fruit = context.getBodyAsJson().mapTo(Fruit.class);

        if (fruit.id != null) {
            context.response().setStatusCode(422).end("Id was invalidly set on request.");
        }

        fruit.save(pool).subscribe(
                createdFruit -> context.response().setStatusCode(201).end(),
                e -> context.response().setStatusCode(500).end(e.getMessage()
                )
        );
    }

    @Route(path = "/fruits/:id", methods = PUT, consumes = "application/json", produces = "application/json")
    public void update(RoutingContext context) {

        Integer id = Integer.valueOf(context.request().getParam("id"));
        Fruit fruit = context.getBodyAsJson().mapTo(Fruit.class);

        if (fruit.name == null) {
            context.response().setStatusCode(422).end( "Fruit Name was not set on request.");
        }

        Fruit.findById(pool, id).subscribe(
                entity -> {
                    entity.name = fruit.name;
                    entity.save(pool).subscribe(
                            updateFruit -> context.response().setStatusCode(200).end(),
                            e -> context.response().setStatusCode(500).end(e.getLocalizedMessage())
                    );
                },
                e -> {
                    if (e instanceof NoSuchElementException)
                        context.response().setStatusCode(404).end("Fruit with id of " + id + " does not exist.");
                    context.response().setStatusCode(500).end(e.getMessage());
                });
    }

    @Route(path = "/fruits/:id", methods = DELETE)
    public void delete(RoutingContext ctx) {
        Integer id = Integer.valueOf(ctx.request().getParam("id"));
        Fruit.delete(pool, id).subscribe(
                rowset -> ctx.response().setStatusCode(204).end(),
                e -> ctx.response().setStatusCode(500).end(e.getMessage()));
    }


}
