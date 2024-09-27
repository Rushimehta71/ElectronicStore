package com.lcwd.electronicstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.lcwd.electronicstore.security.JwtAuthenticationEntryPoint;
import com.lcwd.electronicstore.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    private final String[] PUBLIC_URLS = {

            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs",
//            "/v2/api-docs",
            "/test"


    };

//    @Bean
//    public UserDetailsService userDetailsService() {

    //        UserDetails normal = User.builder()
    //                .username("Ankit")
    //                .password(passwordEncoder().encode("ankit"))
    //                .roles("NORMAL")
    //                .build();
    //
    //        UserDetails admin = User.builder()
    //                .username("Durgesh")
    //                .password(passwordEncoder().encode("durgesh"))
    //                .roles("ADMIN")
    //                .build();
    //users create
    //InMemoryUserDetailsManager- is implementation class of UserDetailService
    //        return new InMemoryUserDetailsManager(normal, admin);
    //    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


    	//
    	//        http.authorizeRequests()
    	//                .anyRequest()
    	//                .authenticated()
    	//                .and().
    	//                formLogin()
    	//                .loginPage("login.html")
    	//                .loginProcessingUrl("/process-url")
    	//                .defaultSuccessUrl("/dashboard")
    	//                .failureUrl("/error")
    	//                .and()
    	//                .logout()
    	//                .logoutUrl("/do-logout");


    	http.csrf(AbstractHttpConfigurer::disable)
    	.authorizeHttpRequests(request ->
    	request.requestMatchers("/auth/login", "/cloudinary/upload")
    	.permitAll()
    	.requestMatchers("/auth/google")
    	.permitAll()
    	.requestMatchers(HttpMethod.POST, "/users")
    	.permitAll()
    	.requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
    	.requestMatchers(PUBLIC_URLS)
    	.permitAll()
    	.requestMatchers(HttpMethod.GET)
    	.permitAll()
    	.anyRequest()
    	.authenticated()
    			)


    	.exceptionHandling(config -> config.authenticationEntryPoint(authenticationEntryPoint))
    	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    	
    	//Here we are telling Spring controller to put our JwtAuthrnticationfilter class do filter method before any req which is not permmited
    	http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    	return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

}