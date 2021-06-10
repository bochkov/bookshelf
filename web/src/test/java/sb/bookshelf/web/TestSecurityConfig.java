package sb.bookshelf.web;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import sb.bookshelf.web.config.CfgSecurity;

@TestConfiguration
@EnableWebSecurity
@Profile("test")
public class TestSecurityConfig extends CfgSecurity {

    public TestSecurityConfig() {
        super(null);
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .passwordEncoder(s -> passwordEncoder().encode(s))
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
