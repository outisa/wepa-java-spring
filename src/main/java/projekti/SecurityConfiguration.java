package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/registration").permitAll()
                .antMatchers(HttpMethod.POST, "/registration").permitAll()
                .antMatchers(HttpMethod.GET, "/search").permitAll()
                .antMatchers(HttpMethod.POST, "/search").permitAll()                
                .antMatchers(HttpMethod.GET, "/accounts/**").permitAll()
                .antMatchers(HttpMethod.GET, "/profilepicture/**").permitAll()
                .antMatchers("/javascript/**").permitAll()
                .antMatchers("/img/**").permitAll()                
                .antMatchers("/").permitAll()                
                .anyRequest().authenticated();
                
                
        http.formLogin()
            .loginPage("/login")
            .failureUrl("/login-error")
            .permitAll()
            .and()
            .logout().logoutUrl("/logout")
            .logoutSuccessUrl("/");
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
    }
}
