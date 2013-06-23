package fr.xebia.yawyl.web;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mongodb.*;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.oauth.OAuthService;

import java.net.UnknownHostException;

public class Module extends AbstractModule {

	public static final String USERS_COLLECTION = "users";

	public static final String NOTIFS_COLLECTION = "notifications";

	@Override
	protected void configure() {
	}

    //mongodb://cloudbees:c0742f0f0d528d07de4ce7ee3ce941c9@dharma.mongohq.com:10077/Sn4vhtpgtmQscYZy66rv6Q

	protected DB getDb() throws UnknownHostException {
		if ("DEV".equals(System.getProperty("XPUA_ENV"))) {
			return new Mongo().getDB("yawl");
		} else {

			String host = System.getProperty("mongoHost");
            System.out.println(host);

			int port = Integer.parseInt(System.getProperty("mongoPort"));

			DB db = new Mongo(host, port).getDB(System.getProperty("mongoDB"));

			String username = System.getProperty("mongoUser");
			String password = System.getProperty("mongoPass");

			db.authenticate(username, password.toCharArray());

			return db;
		}
	}

	@Provides
	@Named(USERS_COLLECTION)
	public DBCollection createUsersCollection() throws UnknownHostException {
		DBCollection collection = getDb().getCollection(USERS_COLLECTION);

		collection.ensureIndex("artists");
		collection.ensureIndex(new BasicDBObject("coordinates", "2d"));

		return collection;
	}

	@Provides
	@Named(NOTIFS_COLLECTION)
	public DBCollection createNotificationsCollection() throws UnknownHostException {
		DB db = getDb();

		if (db.collectionExists(NOTIFS_COLLECTION)) {
			return db.getCollection(NOTIFS_COLLECTION);
		} else {
			DBCollection collection = db.createCollection(NOTIFS_COLLECTION,
					BasicDBObjectBuilder.start("capped", true)
							.add("size", 20).get());

			collection.ensureIndex("date");

			return collection;
		}
	}

	@Provides
	private OAuthService createOAuthServiceForTwitter() {
		return new ServiceBuilder()
				.provider(TwitterApi.class)
				.apiKey("6EK0Es2zfIx4SHaazCNuGg")
				.apiSecret("NJW1RaSzylevlg0Awfv00mRsmr0Tiq3eyRgojHzA")
				.callback("http://x.x.x.x:8080/login/twitter/verify/")
				.build();
	}

}
