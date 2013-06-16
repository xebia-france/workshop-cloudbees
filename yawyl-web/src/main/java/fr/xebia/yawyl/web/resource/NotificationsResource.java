package fr.xebia.yawyl.web.resource;

import com.google.gson.Gson;
import com.google.inject.Inject;
import fr.xebia.yawyl.repository.NotificationsRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/notifications")
public class NotificationsResource {

	private NotificationsRepository repository;

	@Inject
	public NotificationsResource(NotificationsRepository repository) {
		this.repository = repository;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNotifications(@CookieParam("login") String login, @QueryParam("since") Long since) {
		String json = new Gson().toJson(repository.getSince(login, since));

		return Response.ok(json).build();
	}

}
