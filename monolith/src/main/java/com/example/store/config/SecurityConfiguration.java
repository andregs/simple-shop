package com.example.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public void initialize(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
        // here you can customize queries when you already have credentials stored somewhere
        var usersQuery = "select username, password, 'true' from users where username = ?";
        var rolesQuery = "select username, role from users where username = ?";
        auth.jdbcAuthentication()
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
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()

            // 401-UNAUTHORIZED when anonymous user tries to access protected URLs
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))

            // standard login form that sends 204-NO_CONTENT when login is OK and 401-UNAUTHORIZED when login fails
            .and()
                .formLogin()
                .successHandler((req, res, auth) -> res.setStatus(HttpStatus.NO_CONTENT.value()))
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())

            // standard logout that sends 204-NO_CONTENT when logout is OK
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
}
