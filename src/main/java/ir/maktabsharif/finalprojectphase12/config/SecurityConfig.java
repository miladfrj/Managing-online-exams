package ir.maktabsharif.finalprojectphase12.config;


import ir.maktabsharif.finalprojectphase12.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final CustomSuccessHandler customSuccessHandler;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService , CustomSuccessHandler customSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.customSuccessHandler = customSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/css/**", "/js/**","/images/**").permitAll()
                        .requestMatchers("/user/login", "/user/signup", "/user/registration-success").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/student/**").hasAuthority("STUDENT")
                        .requestMatchers("/teacher/** , /question/**").hasAuthority("TEACHER")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")  // Specify URL for login form submission
                        .usernameParameter("username")  // Specify username parameter
                        .passwordParameter("password")  // Specify password parameter
                        .successHandler(customSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // Specify logout URL
                        .logoutSuccessUrl("/user/login?logout")
                        .invalidateHttpSession(true)  // Invalidate session
                        .deleteCookies("JSESSIONID")  // Delete cookies
                        // Redirect to login page after logout
                        .permitAll()
                );

        return http.build();
    }


}
