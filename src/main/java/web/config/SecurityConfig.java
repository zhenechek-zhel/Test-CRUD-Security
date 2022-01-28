package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import web.service.UserService;



/*
Spring ранит данные о пользователе в SecurityContextHolder -> SecurityContext(Хранилище данных) ->
ThreadLocal(переменная) -> объект типа Authenticated -> Principal, Credentials(password), Authorities(права доступа)

 */

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests() // авторизация пользователей по следующим данным:
                .antMatchers("/auth/**").authenticated() // если адресс начигачется с такого корня, то дальше только аунтетифицированный пользователи, остальные части приложения доступны всем
                .antMatchers("/only_for_admins/**").hasRole("ADMIN")
                .antMatchers("/read_profile/**").hasAuthority("READ_PROFILE")
                //.antMatchers("/admin/**").hasAnyRole("ADMIN", "SUPERADMIN")
                .and()
                .formLogin()
                //.loginProcessingUrl("/homelogin") перенаправить стандартный адресс логина
                .and()
                .logout().logoutSuccessUrl("/");

    }


//  In memory
//    @Bean
//    public UserDetailsService users() {
//        UserDetails user = User.builder() // UserDetails - минимальная информация о пользователях(Entity User можно приводить к этому виду)
//                .username("user")
//                .password("{bcrypt}$2a$12$vFl.pCU9s2dJ0YB8lHzHteytiqBTXSbva8GcOVRYrPPyaq6YC.Xc2")
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder() // UserDetails - минимальная информация о пользователях(Entity User можно приводить к этому виду)
//                .username("admin")
//                .password("{bcrypt}$2a$12$tl5ssWsql2mttTfbrPXXQOHp5o54nIUeNy.y/ft/efFH9no7uw4cm")
//                .roles("ADMIN", "USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }



    // jdbc Authentication
//    @Bean
//    public JdbcUserDetailsManager jdbcManager(DataSource dataSource) {
//                UserDetails user = User.builder() // UserDetails - минимальная информация о пользователях(Entity User можно приводить к этому виду)
//                .username("user")
//                .password("{bcrypt}$2a$12$vFl.pCU9s2dJ0YB8lHzHteytiqBTXSbva8GcOVRYrPPyaq6YC.Xc2")
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder() // UserDetails - минимальная информация о пользователях(Entity User можно приводить к этому виду)
//                .username("admin")
//                .password("{bcrypt}$2a$12$tl5ssWsql2mttTfbrPXXQOHp5o54nIUeNy.y/ft/efFH9no7uw4cm")
//                .roles("ADMIN", "USER")
//                .build();

 //       JdbcUserDetailsManager jdbcUsers = new JdbcUserDetailsManager(dataSource);

//        if (jdbcUsers.userExists(user.getUsername())) {
//            jdbcUsers.deleteUser(user.getUsername());
//        }
//        if (jdbcUsers.userExists(admin.getUsername())) {
//            jdbcUsers.deleteUser(admin.getUsername());
//        }
//
//        jdbcUsers.createUser(user);
//        jdbcUsers.createUser(admin);

//        return jdbcUsers;
 //   }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public DaoAuthenticationProvider provider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }
}
