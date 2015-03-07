package com.tahir.project.config;

/**
 * Created by Tahir on 3/7/15.
 */
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.tahir.project")
public class AppConfig {
  @Bean
  public ViewResolver viewResolver() {
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

    //viewResolver.setViewClass(JstlView.class);
    viewResolver.setPrefix("/WEB-INF/views/jsp/");
    viewResolver.setSuffix(".jsp");

    return viewResolver;
  }
  @Bean
  public static PropertyPlaceholderConfigurer properties() {
    PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();

    Resource[] resources =
        new ClassPathResource[] {new ClassPathResource("application.properties")};
    ppc.setLocations(resources);
    ppc.setIgnoreResourceNotFound(false);
    ppc.setIgnoreUnresolvablePlaceholders(false);

    return ppc;
  }
}