package io.agileintelligence.ppmtool.bean;

import java.lang.annotation.Annotation;

import javax.servlet.annotation.WebInitParam;

import org.h2.server.web.WebServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;


@Configuration
public class h2servletRegistration {

	
	@Bean
	@ConditionalOnMissingBean
	public ServletRegistrationBean h2servletRegistrationa() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
		registration.addUrlMappings("/console/*");
		return registration;
	}
	
}


