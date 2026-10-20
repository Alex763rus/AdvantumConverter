package com.example.advantumconverter.config;

import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static com.example.advantumconverter.enums.UserRole.NEED_SETTING;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        // Читаем пользователя из БД при каждом входе, чтобы изменения ролей
        // из админки подхватывались без рестарта сервиса.
        // Логин = поле login (для старых пользователей = chat_id), пароль хранится в БД в виде хеша.
        return username -> {
            var user = userRepository.findUserByLogin(username);
            if (user == null) {
                throw new UsernameNotFoundException("Пользователь не найден");
            }
            if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
                // Пользователи, созданные до перехода на пароли: пароль = chat_id
                user.setPasswordHash(passwordEncoder().encode(String.valueOf(user.getChatId())));
                userRepository.save(user);
            }
            return new CustomUserDetails(username,
                    user.getPasswordHash(),
                    user.getUserRole(),
                    user.getCompany(),
                    Boolean.TRUE.equals(user.getMustChangePassword()));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            if (userDetails.isMustChangePassword()) {
                response.sendRedirect("/change-password");
            } else if (userDetails.getRole() == NEED_SETTING) {
                response.sendRedirect("/setup");
            } else {
                response.sendRedirect("/");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/login", "/setup", "/register").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // 🔐 Только ADMIN
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable); // 🔴 Отключаем CSRF

        return http.build();
    }
}