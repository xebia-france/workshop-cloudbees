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

    protected DB getDb() throws UnknownHostException {
        MongoURI mongoURI = new MongoURI(System.getProperty("MONGOHQ_URL_YAWYL"));

        DB db = mongoURI.connectDB();

        db.authenticate(mongoURI.getUsername(), mongoURI.getPassword());

        return db;
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
