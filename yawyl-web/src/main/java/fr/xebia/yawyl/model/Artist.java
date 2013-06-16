package fr.xebia.yawyl.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Artist {

	public String id;

	public String name;

	public Artist(DBObject artist) {
		this.id = (String) artist.get("id");
		this.name = (String) artist.get("name");
	}

	public Artist(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public DBObject toDBOject() {
		DBObject dbArtist = new BasicDBObject();

		dbArtist.put("id", id);
		dbArtist.put("name", name);

		return dbArtist;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Artist artist = (Artist) o;

		if (id != null ? !id.equals(artist.id) : artist.id != null) return false;
		if (name != null ? !name.equals(artist.name) : artist.name != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;

		result = 31 * result + (name != null ? name.hashCode() : 0);

		return result;
	}

}
