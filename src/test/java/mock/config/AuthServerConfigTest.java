package mock.config;

import mock.accounts.Account;
import mock.accounts.AccountRole;
import mock.accounts.AccountService;
import mock.common.BaseControllerTest;
import mock.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @TestDescription("인증 토큰을 발급받는 테스트")
    public void getAuthToken() throws Exception {

        //Given
        Set<AccountRole> accountRoles = Stream.of(AccountRole.ADMIN, AccountRole.USER).collect(Collectors.toSet());
        String username = "kyj0326@sk.com";
        String password = "youngjae";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(accountRoles)
                .build();
        accountService.saveAccount(account);
        String clientId = "myApp";
        String clientSecret = "pass";
        mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(clientId,clientSecret))//head를 만든거고!
                    .param("username",username)
                    .param("password",password)
                    .param("grant_type","password")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }

}