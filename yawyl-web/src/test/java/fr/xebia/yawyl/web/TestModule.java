package fr.xebia.yawyl.web;

import com.mongodb.DB;
import com.mongodb.Mongo;

import java.net.UnknownHostException;

public class TestModule extends Module {
    protected DB getDb() throws UnknownHostException {
        // utiliser une base locale pour les tests
        return new Mongo("localhost", MongoDBRule.MONGO_PORT).getDB("yawl");
    }
}
