package fr.xebia.yawyl.web.resource;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.xebia.yawyl.model.Artist;
import fr.xebia.yawyl.model.User;
import fr.xebia.yawyl.repository.UserRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Singleton
@Path("/artists")
public class ArtistResource {

	private UserRepository userRepository;

	@Inject
	public ArtistResource(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GET
	@Path("/{id}/fans")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findFans(@PathParam("id") String id) {
		List<User> fans = userRepository.findFansOf(id);

		String json = new Gson().toJson(fans);

		return Response.ok(json).build();
	}

	@GET
	@Path("/near/{longitude}/{latitude}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAtistRankingNear(@PathParam("longitude") String longitude, @PathParam("latitude") String latitude) {
		List<User> users = userRepository.findUsersNear(longitude, latitude);

		Map<String, Integer> scores = Maps.newHashMap();

		for (User user : users) {
			for (Artist artist : user.artists) {
				if (scores.containsKey(artist.name)) {
					scores.put(artist.name, scores.get(artist.name) + 1);
				} else {
					scores.put(artist.name, 1);
				}
			}
		}

		return Response.ok(new Gson().toJson(scores)).build();
	}

}
