package dev.mvc.Book_Review_Blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"dev.mvc"})
public class BookReviewBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookReviewBlogApplication.class, args);
	}

}
