package mock.index;

import mock.common.RestDocsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest // @SpringBootApplication 를 찾아서 그 밑의 모든 빈을 찾아서 등록한다.
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class) // 다른 빈설정파일을 읽어와서 설정하는 방법 중 하나!
public class IndexControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void idex() throws Exception {
        mockMvc.perform(get("/api/"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("_links.events").exists())
                    ;
    }
}
