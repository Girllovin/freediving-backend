package smallITgroup.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class AuthorizationConfiguration {

	private final UserDetailsServiceImpl userDetailsService;

	public AuthorizationConfiguration(UserDetailsServiceImpl userDetailsService) {

		this.userDetailsService = userDetailsService;
	}

	@Bean
	@Order(1)
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		System.out.println("🔧 SecurityFilterChain is being applied");
	
		http
			.securityMatcher("/**")
			.csrf(csrf -> csrf.disable())
			.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
			.authorizeHttpRequests(authorize -> authorize
				// Allow CORS pre-flight
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	
				// ✅ Allow Swagger UI
				.requestMatchers(
                "/account/register",
                "/account/login",
                "/account/recovery/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
				"/swagger-ui.html",
                "/webjars/**",
                "/error"
				).permitAll()
	
				// Require authentication for forum endpoints
				.requestMatchers("/forum/**").authenticated()
	
				// Default: allow all for now
				.anyRequest().permitAll()
			)
			.httpBasic(withDefaults())
			.formLogin(form -> form.disable());
	
		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
