package com.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.AppInitializer;

@Component
public class AppInitializerConfig {
	@Bean
	 @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	 public AppInitializer getAppInitializer() {

	  return new AppInitializer();
	 }
}
