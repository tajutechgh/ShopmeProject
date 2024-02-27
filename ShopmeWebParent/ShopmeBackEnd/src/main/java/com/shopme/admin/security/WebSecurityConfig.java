package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	UserDetailsService adminUserDetailsService() {
		
		return new ShopmeUserDetailsService();
	}
	
	@Bean
	PasswordEncoder adminPasswordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
//	@Bean
//	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//		
//	    return authConfig.getAuthenticationManager();
//	}
	
	@Bean
	DaoAuthenticationProvider adminAuthenticationProvider() {
		
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	     
	    authProvider.setUserDetailsService(adminUserDetailsService());
	    authProvider.setPasswordEncoder(adminPasswordEncoder());
	 
	    return authProvider;
	}
	
	@Bean
	SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(adminAuthenticationProvider());
		
		http.authorizeHttpRequests(auth -> auth
				
		    .requestMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
		    
			.requestMatchers("/states/list_by_country/**").hasAnyAuthority("Admin", "Salesperson")
			
			.requestMatchers("/reviews/**").hasAnyAuthority("Admin", "Assistant")
			
			.requestMatchers("/categories/**", "/brands/**", "/articles/**", "/sections/**").hasAnyAuthority("Admin", "Editor")
			
			.requestMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
			
			.requestMatchers("/products/edit/**", "/products/save", "/products/check_unique").hasAnyAuthority("Admin", "Editor", "Salesperson")
				
			.requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
				
			.requestMatchers("/products/**", "/menus/**", "/articles/**").hasAnyAuthority("Admin", "Editor")
			
			.requestMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
			
			.requestMatchers("/products/detail/**", "/customers/detail/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Assistant")
	
			.requestMatchers("/customers/**", "/orders/**", "/get_shipping_cost", "/reports/**").hasAnyAuthority("Admin", "Salesperson")
			
			.requestMatchers("/orders_shipper/update/**").hasAuthority("Shipper")
			
			.requestMatchers("/reviews/**", "/questions/**").hasAnyAuthority("Admin", "Assistant")
			
		    .anyRequest().authenticated()
		)
		.formLogin(form -> form
				
		    .loginPage("/login")
		    .usernameParameter("email")
		    .permitAll()
		)
		.logout(logout -> logout.permitAll() )
		.rememberMe(rem -> rem
			.key("AbcDefgHijKlmnOpqrs_1234567890")
			.tokenValiditySeconds(7 * 24 * 60 * 60)
		);
		
		http.headers(headers -> headers.frameOptions(f -> f.sameOrigin()));
		
		return http.build();
	}
	
	@Bean
    WebSecurityCustomizer AdminWebSecurityCustomizer() throws Exception {
		
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }

}
