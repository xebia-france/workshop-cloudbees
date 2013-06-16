package fr.xebia.yawyl.repository;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mongodb.*;
import fr.xebia.yawyl.model.User;
import fr.xebia.yawyl.web.Module;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserRepository {

	private final double DEFAULT_LAT = 48.8566140;

	private final double DEFAULT_LNG = 2.35222190;

	@Inject
	@Named(Module.USERS_COLLECTION)
	private DBCollection userCollection;

	public User findByLogin(String login) {
		DBObject user = userCollection.findOne(new BasicDBObject("login", login));

		return user != null ? new User(user) : null;
	}

	public User findOrCreateUser(String login, String city) {
		DBObject searchUser = new BasicDBObject("login", login);

		DBObject user = userCollection.findOne(searchUser);

		if (user != null) {
			return new User(user);
		} else {
			// save new user
			geocodeCity(city, searchUser);

			userCollection.save(searchUser);

			return new User(searchUser);
		}
	}

	private void geocodeCity(String city, DBObject searchUser) {
		final Geocoder geocoder = new Geocoder();

		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(city + ", France").setLanguage("fr").getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);

		double lat = DEFAULT_LAT;
		double lng = DEFAULT_LNG;

		List<GeocoderResult> results = geocoderResponse.getResults();

		if (results.size() != 0) {
			GeocoderGeometry geometry = results.get(0).getGeometry();

			lat = geometry.getLocation().getLat().doubleValue();
			lng = geometry.getLocation().getLng().doubleValue();
		}

		BasicDBList basicDBList = new BasicDBList();

		basicDBList.add(lng);
		basicDBList.add(lat);

		searchUser.put("coordinates", basicDBList);
	}

	public List<User> findFansOf(String artistId) {
		DBObject query = new BasicDBObject();
		query.put("artists.id", artistId);

		List<User> users = new ArrayList<User>();

		DBCursor usersCursor = userCollection.find(query);

		for (DBObject dbObject : usersCursor) {
			users.add(new User(dbObject));
		}

		return users;
	}

	public void save(User user) {
		userCollection.save(user.toDBOject());
	}

	public List<User> findUsersNear(String longitude, String latitude) {
		DBObject query = new BasicDBObject();
		query.put("coordinates", new BasicDBObject("$near", new Double[]{Double.valueOf(longitude), Double.valueOf(latitude)}));

		DBCursor dbCursor = userCollection.find(query);

		return User.toUsers(dbCursor);
	}

}
