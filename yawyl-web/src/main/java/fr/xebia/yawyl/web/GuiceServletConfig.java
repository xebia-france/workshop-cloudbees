package fr.xebia.yawyl.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

public class GuiceServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		final Map<String, String> params = new HashMap<String, String>();

		params.put("javax.ws.rs.Application", "fr.xebia.yawyl.web.JerseyApplication");

		return Guice.createInjector(
				new Module(),
				new ServletModule() {

					@Override
					protected void configureServlets() {
						serve("/resources/*").with(GuiceContainer.class, params);
					}

				}
		);
	}

}
