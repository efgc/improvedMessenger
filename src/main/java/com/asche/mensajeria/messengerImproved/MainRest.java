package com.asche.mensajeria.messengerImproved;
import javax.servlet.ServletConfig;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import io.swagger.jaxrs.config.BeanConfig;

@ApplicationPath("/api")
public class MainRest extends Application {

	public MainRest(@Context ServletConfig servletConfig) {
		super();

		BeanConfig beanConfig = new BeanConfig();

		beanConfig.setVersion("1.0.0");
		beanConfig.setTitle("Messenger API");
		beanConfig.setBasePath("/messengerImproved/api");
		beanConfig.setResourcePackage("com.asche.mensajeria");
		beanConfig.setScan(true);
	}

}
