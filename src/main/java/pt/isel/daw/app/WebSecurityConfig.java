package pt.isel.daw.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("dsUsers")
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN") //role should not start with 'ROLE_' since it is automatically inserted. Got 'ROLE_ADMIN'
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/html/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/user/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/html/user/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().permitAll()
                .and()
                .csrf()      //se não for introduzido .csrf().disable() é lançado um erro:
                .disable()   //"status":403,"error":"Forbidden","message":"Could not verify the provided CSRF token because your session was not found."
                .httpBasic();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from credentials where username=?")
                .authoritiesByUsernameQuery("select username, role from credentials c, user u where c.user_id = u.user_id and username=?");
    }
}