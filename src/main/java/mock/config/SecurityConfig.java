package mock.config;

import mock.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity //이거 다는순간 스프링시큐리티 적용 됨
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public TokenStore tokenStore(){
        return new InMemoryTokenStore();
    }

//    AuthorizationServer: OAuth2 토큰 발행(/oauth/token) 및 토큰 인증(/oauth/authorize)
//          Oder 0 (리소스 서버 보다 우선 순위가 높다.)
//    ResourceServer: 리소스 요청 인증 처리 (OAuth 2 토큰 검사)
//          Oder 3 (이 값은 현재 고칠 수 없음)

    @Bean // 이거를 오버라이딩해서 bean을 붙여야 다른데서 참조할 수 있도록 노출이 된다.(위에 두개에서))
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override // 어떻게 만들꺼냐! 재정의해야지!
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    @Override//필터를 적용할 지 말지 //아래 경로 요청은 스프링 시큐리티가 적용이 안된다.
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/docs/index.html");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

//    @Override //http를 이용해서 위와같은 처리를 한거임 . 웹에서 하는게 근데 덜 일을 함. 로그보면 앎
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .mvcMatchers("/docs/index.html").anonymous()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).anonymous();
//    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .anonymous()
//                    .and()
//                .formLogin()
//                    .and()
//                .authorizeRequests()
//                    .mvcMatchers(HttpMethod.GET, "/api/**").authenticated()
//                    .anyRequest().authenticated();
//    }
}
