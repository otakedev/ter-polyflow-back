package fr.polytech.webservices;

import javax.servlet.http.Cookie;

import com.google.inject.internal.util.ImmutableList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import fr.polytech.entities.models.Role;
import fr.polytech.webservices.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.active}")
    private boolean isSecurityActive;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must
        // not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Cookie sessionCookie = new Cookie("JSESSIONID", null);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(0);

        http.csrf().disable();

        // @formatter:off
        if (isSecurityActive) {
            http.cors().and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.OPTIONS, "*").permitAll()
                        .antMatchers("/login", "/logout").permitAll()
                        .antMatchers("/api/admin/**").hasAuthority(Role.ADMIN.toString()).and()
                            .exceptionHandling()
                            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and()
                    .formLogin()
                        .permitAll().loginProcessingUrl("/login")
                        .successHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value()))
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler()).and()
                    .logout().permitAll()
                        .addLogoutHandler(new CookieClearingLogoutHandler(sessionCookie))
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
        } else {
            http.cors().and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.OPTIONS, "*").permitAll()
                        .antMatchers("/login", "/logout").permitAll()
                        .anyRequest().permitAll().and()
                    .formLogin()
                        .permitAll().loginProcessingUrl("/login")
                        .successHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value()))
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler()).and()
                    .logout().permitAll()
                        .addLogoutHandler(new CookieClearingLogoutHandler(sessionCookie))
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
        }
        // @formatter:on
    }
}
