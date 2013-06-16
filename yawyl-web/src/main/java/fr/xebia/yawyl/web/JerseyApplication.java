package fr.xebia.yawyl.web;


import fr.xebia.yawyl.web.resource.*;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class JerseyApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		classes.add(TwitterLoginResource.class);
		classes.add(UserResource.class);
		classes.add(ArtistResource.class);
		classes.add(NotificationsResource.class);
		classes.add(ConcertResource.class);

		return classes;
	}

}

