package dev.mvc.Book_Review_Blog;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.mvc.review.Review;
import dev.mvc.tool.Tool;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Windows: path = "C:/kd/deploy/resort/review/storage";
        // ▶ file:///C:/kd/deploy/resort_v2sbm3c_blog/review/storage
      
        // Ubuntu: path = "/home/ubuntu/deploy/resort/review/storage";
        // ▶ file:////home/ubuntu/deploy/resort_v2sbm3c_blog/review/storage
      
        // JSP 인식되는 경로: http://localhost:9091/review/storage";
        // C:kd/deploy/resort/review/storage -> /review/storage -> http://localhost:9091/review/ctorage;
        registry.addResourceHandler("/review/storage/**").addResourceLocations("file:///" +  Review.getUploadDir());
        
        // JSP 인식되는 경로: http://localhost:9091/attachfile/storage";
        // registry.addResourceHandler("/review/storage/**").addResourceLocations("file:///" +  Tool.getOSPath() + "/attachfile/storage/");
        
        // JSP 인식되는 경로: http://localhost:9091/users/storage";
        // registry.addResourceHandler("/review/storage/**").addResourceLocations("file:///" +  Tool.getOSPath() + "/users/storage/");
    }
 
}