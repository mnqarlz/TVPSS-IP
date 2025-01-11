package config;

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
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .passwordEncoder(passwordEncoder())
            .usersByUsernameQuery(
                "SELECT email AS username, password FROM users WHERE email = ?")
            .authoritiesByUsernameQuery(
                "SELECT email AS username, CONCAT('ROLE_', role) AS authority FROM users WHERE email = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(requests -> requests
                .antMatchers("/1-SuperAdmin/**").hasRole("SUPER_ADMIN")
                .antMatchers("/2-StateAdmin/**").hasRole("STATE_ADMIN")
                .antMatchers("/3-PPDAdmin/**").hasRole("PPD_ADMIN")
                .antMatchers("/4-SchoolAdmin/**").hasRole("SCHOOL_ADMIN")
                .antMatchers("/login/admin", "/css/**", "/js/**", "/public/**").permitAll()
                .anyRequest().authenticated())
            .formLogin(login -> login
                .loginPage("/login/admin")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/redirect", true)
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login/admin?logout")
                .permitAll())
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Default session creation
            .maximumSessions(1) // Only 1 active session per user
            .expiredUrl("/login/admin?expired") // Redirect when session expires
            .and()
            .sessionFixation().migrateSession(); // Prevent session fixation attacks
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}