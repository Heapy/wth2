package by.heap;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;

/**
 * For hosting static files inside war.
 *
 * @author Ibragimov Ruslan
 */
@Configuration
@EnableWebMvc
public class HeapWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // TODO: Change to actual values
        registry.addResourceHandler("/assets/**").addResourceLocations("/dist/assets/");
        registry.addResourceHandler("/*.html").addResourceLocations("/dist/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/index.html");
    }

}
