package net.engineeringdigest.journalApp.config;

import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceimpl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http
//                .authorizeRequests() // This tells Spring Security to start authorizing the requests
//                    .antMatchers("/journal/**").permitAll() // Specifies that HTTP requests matching the path should be permitted for all users, whether they are authenticated or not.
//                    .anyRequest().authenticated() // Remaining requests should be authenticated, they have to provide valid credentials to access these endpoints
//                .and()// we are going back to http
//                .formLogin(); // form-based authentication

        http.authorizeRequests()
                .antMatchers("/journal/**","/user/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .httpBasic();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();

        /*
        Some applications mix Basic Auth with Session Management for various reasons.
        This isn't standard behavior and requires additional setup and logic. In such scenarios, once the
        user's credentials are verified via basic auth, a session might be established and the client is
        provided a session cookie. This way, the client won't need to send the Auth header with every request,
        and the server can rely on the session cookie to identify tha authenticated user.
         */
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceimpl).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
