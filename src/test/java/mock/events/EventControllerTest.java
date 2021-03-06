package mock.events;

import mock.accounts.Account;
import mock.accounts.AccountRepository;
import mock.accounts.AccountRole;
import mock.accounts.AccountService;
import mock.common.BaseControllerTest;
import mock.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTest extends BaseControllerTest {


    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Before
    public void Setup(){
            eventRepository.deleteAll();
            accountRepository.deleteAll();
    }

    @Test
    @TestDescription(value = "정상적으로 이벤트를 발생 시키는 이벤트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

//        Mockito.when(repository.save(event)).thenReturn(event);
//        컨트롤러에는 dto를 받아서 변환해서 호출 했기 때문에 위의 것은 null을 받기 때문에 에러난다.
        mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION,"Bearer" + getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-events").exists())
                .andDo(document("create-event",     //요고 하나 추가했더니 도큐맨트가 만들어졌다.
                        links(          //요고 넣으니까 links.adoc가 추가됐다.
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query-events"),
                                linkWithRel("update-events").description("link to update an existing event"),
                                linkWithRel("profile").description("link to profile")

                        ),
                        requestHeaders(  //요고 넣으니까 header추가됐다
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields( //요고 넣으니까 requestField추가됐다.
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrolmment")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        //relaxedResponseFields() 응답의 일부분만 하고싶을 때
                        relaxedResponseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrolmment"),
                                fieldWithPath("free").description("it tells if this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline event or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event list"),
                                fieldWithPath("_links.update-events.href").description("link to update existing event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));


    }

    private String getBearerToken() throws Exception {
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
        ResultActions perform = mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))//head를 만든거고!
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password"));
        String responsebody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(responsebody).get("access_token").toString();

    }

    @Test
    @TestDescription(value = "입력받을 수 없는 값을 사용한 경우에 에러가 발생하는 이벤트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                .header(HttpHeaders.AUTHORIZATION,"Bearer" + getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription(value = "입력값이 비어있는 경우에 에러가 발생하는 이벤트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto event = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION,"Bearer" + getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isBadRequest());


    }

    @Test
    @TestDescription(value = "입력값이 잘못 된 경우에 에러가 발생하는 이벤트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2010, 11, 26, 14, 21))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(post("/api/events/")
                .header(HttpHeaders.AUTHORIZATION,"Bearer" + getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists());
    //테스트 꼼꼼하게 다 해야하지만~~~~~~~~~ 글로벌에러만있을 떄,, 두개다있을 떄 등등 우리는 시간관계상!!

    }

    @Test
    @TestDescription("30개 이벤트 10개씩 두번째 페이지 조회하기")
    public void queryEvent() throws Exception {
        //Given
        IntStream.range(0,30).forEach(this::generateEvent);

        //When
        mockMvc.perform(get("/api/events")
                        .param("page","1")
                        .param("size","10")
                        .param("sort","name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"));


    }

    @Test
    @TestDescription("기존의 이벤트 하나만 조회하기")
    public void getEvent() throws Exception{
        Event event = generateEvent(100);
        mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"));
    }
    @Test
    @TestDescription("없는 이벤트 조회해서 404받기!")
    public void getEvent404() throws Exception{

        mockMvc.perform(get("/api/events/12314"))
                .andExpect(status().isNotFound());
    }

    @Test
    @TestDescription("UPDATE 이벤트 정상수행")
    public void updateEvent() throws Exception{

        Event event = generateEvent(100);
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        String updateEvent = "Update Event";
        eventDto.setName(updateEvent);

        mockMvc.perform(put("/api/events/{id}",event.getId())
                            .header(HttpHeaders.AUTHORIZATION,"Bearer" + getBearerToken())
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(updateEvent))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"));

    }

    @Test
    @TestDescription("UPDATE 이벤트 입력값이 비어있는 경우 비정상수행")
    public void updateEvent400_empty() throws Exception{

        Event event = generateEvent(100);
        EventDto eventDto = new EventDto();

        mockMvc.perform(put("/api/events/{id}",event.getId())
                .header(HttpHeaders.AUTHORIZATION,"Bearer" + getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("UPDATE 이벤트 입력값이 잘못 된 경우 비정상수행")
    public void updateEvent400_Wrong() throws Exception{

        Event event = generateEvent(100);
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setMaxPrice(2000);
        eventDto.setBasePrice(10000);

        mockMvc.perform(put("/api/events/{id}",event.getId())
                .header(HttpHeaders.AUTHORIZATION,"Bearer" + getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("UPDATE 존재 하지 않는 이벤트 비정상수행")
    public void updateEvent404() throws Exception{

        Event event = generateEvent(100);
        EventDto eventDto = modelMapper.map(event, EventDto.class);


        mockMvc.perform(put("/api/events/12312312")
                .header(HttpHeaders.AUTHORIZATION,"Bearer" + getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Event generateEvent(int i) {
        Event event = Event.builder()
                .name("Event"+i)
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();
        return eventRepository.save(event);
    }

}

//    Event 생성 API 구현: 입력값 이외에 에러 발생
//        ObjectMapper 커스터마이징
//        spring.jackson.deserialization.fail-on-unknown-properties=true
//
//        테스트 할 것
//        입력값으로 누가 id나 eventStatus, offline, free 이런 데이터까지 같이 주면?
//        Bad_Request로 응답 vs 받기로 한 값 이외는 무시


//    Event 생성 API 구현: 201 응답 받기
//
//@RestController
//@ResponseBody를 모든 메소드에 적용한 것과 동일하다.
//
//        ResponseEntity를 사용하는 이유
//        응답 코드, 헤더, 본문 모두 다루기 편한 API
//
//        Location URI 만들기
//        HATEOS가 제공하는 linkTo(), methodOn() 사용
//
//        객체를 JSON으로 변환
//        ObjectMapper 사용
