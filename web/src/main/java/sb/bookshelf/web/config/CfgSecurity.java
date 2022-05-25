package sb.bookshelf.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import sb.bookshelf.web.dao.AccountDao;

@Configuration
@EnableWebSecurity
@Profile("production")
@RequiredArgsConstructor
public class CfgSecurity {

    private final AccountDao accountDao;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authenticationProvider(authProvider())
                .authorizeRequests()
                .antMatchers("/api/save/", "/api/delete/").authenticated()
                .antMatchers("/**").permitAll()
                .and().httpBasic()
                .and().csrf().disable()
                .build();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            var acc = accountDao.findByUsername(username);
            if (acc != null) {
                return new User(acc.getUsername(), acc.getPassword(),
                        true, true, true, true,
                        AuthorityUtils.createAuthorityList("USER")
                );
            }
            throw new UsernameNotFoundException(String.format("Could not find user '%s'", username));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
