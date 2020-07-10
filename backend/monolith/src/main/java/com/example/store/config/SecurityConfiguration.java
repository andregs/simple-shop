package com.example.store.config;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;

    @Autowired
    public void initialize(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
        // here you can customize queries when you already have credentials stored somewhere
        var usersQuery = "select username, password, 'true' from users where username = ?";
        var rolesQuery = "select username, role from users where username = ?";
        auth.jdbcAuthentication() // FIXME fix sonar issue
                .dataSource(dataSource)
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
        ;
    }

    /**
     * @see "https://stackoverflow.com/a/62378270/259237"
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // all URLs are protected, except 'POST /login' so anonymous user can authenticate
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login", "/users").permitAll()
                .anyRequest().authenticated()

            // 401-UNAUTHORIZED when anonymous user tries to access protected URLs
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))

            // login form that sends 200 when login is OK and 401-UNAUTHORIZED when login fails
            .and()
                .formLogin()
                .successHandler(getAuthenticationSuccessHandler())
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())

            // logout endpoint that sends 204-NO_CONTENT when logout is OK
            .and()
                .logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))

            // add CSRF protection to all URLs
            // TODO firefox gives me Cookie “XSRF-TOKEN” will be soon rejected because it has the “sameSite”
            //  attribute set to “none” or an invalid value, without the “secure” attribute. To know more about the
            //  “sameSite“ attribute, read https://developer.mozilla.org/docs/Web/HTTP/Headers/Set-Cookie/SameSite
            .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        ;
    }

    private AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return (req, res, auth) -> {
            var body = objectMapper.writeValueAsString(auth.getPrincipal());
            res.setStatus(HttpStatus.OK.value());
            res.getWriter().write(body);
        };
    }
}
