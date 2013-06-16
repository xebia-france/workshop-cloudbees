package fr.xebia.yawyl.model;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public class User {

	private String id;

	public String login;

	public double latitude;

	public double longitude;

	public List<Artist> artists = newArrayList();

	public Set<String> badges = newHashSet();

	public User(DBObject dbUser) {
		ObjectId mongoId = (ObjectId) dbUser.get("_id");

		if (mongoId != null) {
			this.id = mongoId.toString();
		}

		this.login = (String) dbUser.get("login");
		this.longitude = (Double) ((BasicDBList) dbUser.get("coordinates")).get(0);
		this.latitude = (Double) ((BasicDBList) dbUser.get("coordinates")).get(1);

		BasicDBList dbArtists = (BasicDBList) dbUser.get("artists");

		if (dbArtists != null) {
			for (Object artist : dbArtists) {
				artists.add(new Artist((DBObject) artist));
			}
		}

		BasicDBList dbBadges = (BasicDBList) dbUser.get("badges");

		if (dbBadges != null) {
			for (Object badge : dbBadges) {
				badges.add((String) badge);
			}
		}
	}

	public void addArtist(Artist artist) {
		if (!artists.contains(artist)) {
			artists.add(artist);
		}
	}

	public DBObject toDBOject() {
		DBObject dbUser = new BasicDBObject();

		dbUser.put("_id", new ObjectId(id));
		dbUser.put("login", login);
		dbUser.put("coordinates", createDBCoordinates());
		dbUser.put("artists", createDBListArtists());
		dbUser.put("badges", createDBListBadges());

		return dbUser;
	}

	private DBObject createDBCoordinates() {
		BasicDBList dbList = new BasicDBList();

		dbList.add(longitude);
		dbList.add(latitude);

		return dbList;
	}

	private BasicDBList createDBListArtists() {
		BasicDBList dBListArtists = new BasicDBList();

		for (Artist artist : artists) {
			dBListArtists.add(artist.toDBOject());
		}

		return dBListArtists;
	}

	private BasicDBList createDBListBadges() {
		BasicDBList dBListBadges = new BasicDBList();

		for (String badge : badges) {
			dBListBadges.add(badge);
		}

		return dBListBadges;
	}

	public static List<User> toUsers(DBCursor dbCursor) {
		List<User> users = new ArrayList<User>();

		for (DBObject dbObject : dbCursor) {
			users.add(new User(dbObject));
		}

		return users;
	}

	public void updateBadges() {
		List<String> artistsId = newArrayList();

		for (Artist artist : artists) {
			artistsId.add(artist.id);
		}

		List<String> start80Artists = newArrayList("ARBEOHF1187B9B044D");
		List<String> discofanArtists = newArrayList("AR23C041187FB4D534", "ARBEOHF1187B9B044D");

		if (artistsId.containsAll(start80Artists)) {
			this.badges.add("start80");
		}

		if (artistsId.containsAll(discofanArtists)) {
			this.badges.add("discoFan");
		}
	}

}
