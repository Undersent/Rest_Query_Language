package com.example.demo;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.request.RequestContextListener;


/**

 * class to run up a Jetty Server (on http://localhost:8082/spring-rest-query-language)
 *
 */
@EnableScheduling
@EnableAutoConfiguration
@ComponentScan("com.example.demo.web")
@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DemoApplication.class);
	}

	@Override
	public void onStartup(ServletContext sc) throws ServletException {
		// Manages the lifecycle of the root application context
		sc.addListener(new RequestContextListener());
	}

	public static void main(final String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
