package smallITgroup.security;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import smallITgroup.accounting.dao.UserAccountRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
@Service("customSecurity")
@RequiredArgsConstructor
public class CustomWebSecurity {
	final UserAccountRepository userAccountRepository;
	private final JwtAuthFilter jwtAuthFilter;

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
	
//	  @Bean
//		public SecurityFilterChain configure(HttpSecurity http) throws Exception {
//			System.out.println("🔧 SecurityFilterChain is being applied");
//
//			http
//				.securityMatcher("/**")
//				.csrf(csrf -> csrf.disable())
//				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
//				.authorizeHttpRequests(authorize -> authorize
//					.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//					.requestMatchers(
//						"/account/register",
//						"/account/login",
//						"/account/recovery/**",
//						"/swagger-ui/**",
//						"/v3/api-docs/**",
//						"/swagger-resources/**",
//						"/swagger-ui.html",
//						"/webjars/**",
//						"/error"
//					).permitAll()
//					.requestMatchers("/forum/**").authenticated()
//					.anyRequest().permitAll()
//				)
//				.httpBasic().disable()
//				.formLogin(form -> form.disable())
//				// 🔐 ВСТАВЛЯЕМ JWT-фильтр перед UsernamePasswordAuthenticationFilter
//				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//			return http.build();
//		}


	    // CORS is now centrally configured in AuthorizationConfiguration.corsConfigurationSource()
	    
//	    @Bean
//	    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//	        return authConfig.getAuthenticationManager();
//	    }
}
