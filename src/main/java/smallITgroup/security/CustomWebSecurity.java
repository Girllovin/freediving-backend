package smallITgroup.security;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import smallITgroup.accounting.dao.UserAccountRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;


@Configuration
@Service("customSecurity")
@RequiredArgsConstructor
public class CustomWebSecurity {
	final UserAccountRepository userAccountRepository;

	public boolean checkPostAuthor(String postId, String userName) {
//		UserAccount post;
//		try {
//			post = UserAccountRepository.findById(postId).orElse(null);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return post != null && userName.equalsIgnoreCase(post.getAuthor());
		return false;
	}
	
	  @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .cors().and()
	            .csrf().disable()
	            .authorizeHttpRequests(auth -> auth
	                .anyRequest().permitAll()
	            );

	        return http.build();
	    }

	    @Bean
	    public CorsFilter corsFilter() {
	        CorsConfiguration config = new CorsConfiguration();
	        config.setAllowedOrigins(List.of(
	            "http://localhost:3000",            
	            "https://freediving-frontend.onrender.com" 
	        ));
	        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	        config.setAllowedHeaders(List.of("*"));
	        config.setAllowCredentials(true); 

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", config);

	        return new CorsFilter(source);
	    }
}
