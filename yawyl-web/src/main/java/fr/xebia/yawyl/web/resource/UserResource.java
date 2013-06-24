package fr.xebia.yawyl.web.resource;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.xebia.yawyl.model.Artist;
import fr.xebia.yawyl.model.User;
import fr.xebia.yawyl.repository.NotificationsRepository;
import fr.xebia.yawyl.repository.UserRepository;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path("/users")
public class UserResource {

	private UserRepository userRepository;

	private NotificationsRepository notificationsRepository;

	@Inject
	public UserResource(UserRepository userRepository, NotificationsRepository notificationsRepository) {
		this.userRepository = userRepository;
		this.notificationsRepository = notificationsRepository;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response login(@FormParam("login") String login, @FormParam("city") String city) {
		User user = userRepository.findOrCreateUser(login, city);

		String json = new Gson().toJson(user);

		return Response.ok(json).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/artist")
	public Response addArtist(@FormParam("login") String login, @FormParam("id") String id, @FormParam("name") String name) {
		User user = userRepository.findByLogin(login);

		user.addArtist(new Artist(id, name));

		user.updateBadges();

		userRepository.save(user);
		notificationsRepository.add(NotificationsRepository.newFavoriteArtistNotification(login, name));

		String json = new Gson().toJson(user);

		return Response.ok(json).build();
	}

}
