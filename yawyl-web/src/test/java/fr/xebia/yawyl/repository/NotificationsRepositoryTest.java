package fr.xebia.yawyl.repository;

import com.google.inject.Inject;
import com.mongodb.*;
import fr.xebia.yawyl.GuiceJUnitRunner;
import fr.xebia.yawyl.web.MongoDBRule;
import fr.xebia.yawyl.web.TestModule;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({TestModule.class})
@Ignore
public class NotificationsRepositoryTest {
    @Rule
    public MongoDBRule mongoDb = new MongoDBRule();

    @Inject
    NotificationsRepository repository;


    @Before
    public void purgeData() {
        DBCollection collection = repository.getCollection();
        collection.getDB().command(new BasicDBObject("emptycapped", collection.getName()));
        assertThat(collection.count()).isEqualTo(0);
    }

    @Test
    public void shouldCreateCappedCollection() {
        assertThat(repository.getCollection().isCapped()).isTrue();
    }

    @Test
    public void testCreateNotification() throws Exception {
        final String login = "Nick";
        final String msgNotif = "Artiste Jonny ajouté !";
        final long oldCount = repository.getCollection().count();
        repository.add(NotificationsRepository.newSimpleNotification(login, msgNotif));
        assertThat(repository.getCollection().count()).isEqualTo(oldCount + 1);
    }

    @Test
    public void getLatest_should_not_return_old_notifications() {
        Date date = new Date();
        Long since = date.getTime();
        insertNotification(new Date(since-1), "too old, should not appear", "nick");
        insertNotification(new Date(since+1), "test", "nick");
        insertNotification(new Date(since+1), "test", "other");
        List<DBObject> latests = repository.getSince("other", since);
        assertThat(latests.size()).isEqualTo(1);
        assertThat(latests.get(0).get("message")).isEqualTo("test");
    }

    private void insertNotification(Date date, String message, String login) {
        DBObject notifDbObject = BasicDBObjectBuilder.start("date", date).add("message", message).add("login", login).get();
        repository.getCollection().insert(notifDbObject);
    }
}
