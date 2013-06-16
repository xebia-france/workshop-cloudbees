package fr.xebia.yawyl.web;

import de.flapdoodle.embedmongo.MongoDBRuntime;
import de.flapdoodle.embedmongo.config.MongodConfig;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;

import static de.flapdoodle.embedmongo.distribution.Version.V2_1_0;

public class MongoDBRule implements TestRule {
    public static final int MONGO_PORT = 27017;

    static {
        try {
            MongoDBRuntime.getDefaultInstance().prepare(new MongodConfig(V2_1_0, MONGO_PORT, false)).start();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
            }
        };
    }
}
