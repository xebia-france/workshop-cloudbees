package fr.xebia.yawyl.repository;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mongodb.*;
import fr.xebia.yawyl.web.Module;

import javax.inject.Singleton;
import java.util.Date;
import java.util.List;

@Singleton
public class NotificationsRepository {

	@Inject
	@Named(Module.NOTIFS_COLLECTION)
	private DBCollection collection;

	/**
	 * passer par les méthodes statiques pour créer les dbobjects
	 */
	public void add(DBObject notifDbObject) {
		collection.insert(notifDbObject);
	}

	public List<DBObject> getSince(String login, Long since) {
		Date minDate = new Date(since);

		DBCursor notifications = collection.find(
				QueryBuilder.start("date").greaterThan(minDate)
						.and("login").notEquals(login).get()
		);

		return notifications.toArray();
	}

	public static DBObject newSimpleNotification(String login, String message) {
		return baseNotificationBuilder(login, "simple")
				.add("message", message).get();
	}

	public static DBObject newFavoriteArtistNotification(String login, String artistName) {
		return baseNotificationBuilder(login, "favoriteArtist")
				.add("artist", artistName).get();
	}

	private static BasicDBObjectBuilder baseNotificationBuilder(String login, String messageType) {
		return BasicDBObjectBuilder
				.start("date", new Date())
				.add("login", login)
				.add("type", messageType);
	}

}
