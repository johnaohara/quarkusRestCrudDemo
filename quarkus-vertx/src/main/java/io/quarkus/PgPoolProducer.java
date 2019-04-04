package io.quarkus;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import io.reactiverse.pgclient.PgPoolOptions;
import io.reactiverse.reactivex.pgclient.PgClient;
import io.reactiverse.reactivex.pgclient.PgPool;
import io.vertx.reactivex.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class PgPoolProducer {

	@ConfigProperty(name = "rest.crud.host", defaultValue = "localhost")
	private String host;

	@ConfigProperty(name = "rest.crud.port", defaultValue = "5432")
	private Integer port;

	@ConfigProperty(name = "rest.crud.database", defaultValue = "rest-crud")
	private String database;

	@ConfigProperty(name = "rest.crud.user", defaultValue = "restcrud")
	private String user;

	@ConfigProperty(name = "rest.crud.password", defaultValue = "restcrud")
	private String password;

	@Produces
	public PgPool getClient() {
		PgPoolOptions options = new PgPoolOptions()
				  .setPort(port)
				  .setHost(host)
				  .setDatabase(database)
				  .setUser(user)
				  .setPassword(password)
				  .setMaxSize(50);

		// Create the client pool
        if (Vertx.currentContext() != null) {
            return PgClient.pool(Vertx.currentContext().owner(), options);
        }else{
            return PgClient.pool(options);
        }
	}
}
