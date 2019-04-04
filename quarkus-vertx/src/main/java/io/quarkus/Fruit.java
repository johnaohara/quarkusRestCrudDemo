package io.quarkus;

import io.reactiverse.pgclient.Row;
import io.reactiverse.reactivex.pgclient.PgPool;
import io.reactiverse.reactivex.pgclient.PgRowSet;
import io.reactiverse.reactivex.pgclient.Tuple;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Fruit {

    public String name;
    public Integer id;
    
    public Fruit() {}
    
	public Fruit(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public static Fruit fromRow(Row row) {
		return new Fruit(row.getInteger("id"), row.getString("name"));
	}

	public Single<Fruit> save(PgPool pool) {
		if(id == null)
			return pool.rxPreparedQuery("SELECT nextval('known_fruits_id_seq') AS id")
					.map(rowset -> rowset.iterator().next().getInteger("id"))
					.flatMap(id -> pool.rxPreparedQuery("INSERT INTO fruit (id, name) VALUES ($1, $2)", Tuple.of(id, name))
							.map(rowset -> {
								this.id = id;
								return this;
							}));
		else
			return pool.rxPreparedQuery("UPDATE known_fruits SET name = $2 WHERE id = $1", Tuple.of(id, name))
					.map(rowset -> this);
	}

	public static Observable<Fruit> findAll(PgPool pool) {
		return pool.rxQuery("SELECT * FROM known_fruits ORDER BY name")
				.flatMapObservable(rowset -> Observable.fromIterable(rowset.getDelegate()))
    			.map(row -> Fruit.fromRow(row));
	}

	public static Single<Fruit> findById(PgPool pool, Integer id) {
		return pool.rxPreparedQuery("SELECT * FROM known_fruits WHERE id = $1", Tuple.of(id))
				.flatMapObservable(rowset -> Observable.fromIterable(rowset.getDelegate()))
    			.firstOrError()
    			.map(row -> Fruit.fromRow(row));
	}

	public static Single<PgRowSet> delete(PgPool pool, Integer id) {
		return pool.rxPreparedQuery("DELETE FROM known_fruits WHERE id = $1", Tuple.of(id));
	}
}
