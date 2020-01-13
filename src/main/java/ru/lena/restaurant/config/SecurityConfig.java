package ru.lena.restaurant.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.lena.restaurant.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public SecurityConfig(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/registration**",
                        "/webjars/**",
                        "/js/**",
                        "/css/**",
                        "/img/**",
                        "/images/**",
                        "/vendor/**",
                        "/fonts/**",
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.GET,"/rest/admin/users/**").hasRole("ADMIN")
                .antMatchers("/rest/admin/dishes/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/rest/admin/restaurants/**").hasRole("USER")
                .antMatchers(HttpMethod.POST,"/rest/admin/restaurants/**/vote").hasRole("USER")
                .antMatchers(HttpMethod.POST,"/rest/admin/restaurants/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/rest/admin/restaurants/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/rest/admin/restaurants/**").hasRole("ADMIN")
                .and().authorizeRequests()
                .antMatchers("/rest/profile/register").anonymous()

                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .failureForwardUrl("/login?error=true")
                .defaultSuccessUrl("/restaurants")
                .and().logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .permitAll()
                .and().httpBasic()
                .and().csrf().disable();

    }

    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userServiceImpl)
                .passwordEncoder(encoder())
                .and()
                .authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userServiceImpl);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }
}
