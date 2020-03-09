package mock.config;

import mock.accounts.Account;
import mock.accounts.AccountRole;
import mock.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Set<AccountRole> accountRoles = Stream.of(AccountRole.ADMIN, AccountRole.USER).collect(Collectors.toSet());
                Account account = Account.builder()
                        .email("kyj0326@sk.com")
                        .password("youngjae")
                        .roles(accountRoles)
                        .build();
                accountService.saveAccount(account);
                System.out.println("만들어짐? ");


            }
        };
    }
}
