package sb.bookshelf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class CfgSecurity extends WebSecurityConfigurerAdapter {

    private final AccountDao accountDao;

    @Autowired
    public CfgSecurity(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authenticationProvider(authProvider())
                .authorizeRequests()
                .antMatchers("/api/count", "/api/list", "/api/search", "/api/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Account acc = accountDao.findByUsername(username);
            if (acc != null) {
                return new User(acc.getUsername(), acc.getPassword(),
                        true, true, true, true,
                        AuthorityUtils.createAuthorityList("USER"));
            }
            throw new UsernameNotFoundException(
                    String.format("Could not find user '%s'", username));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
