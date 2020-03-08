package mock.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder @NoArgsConstructor @AllArgsConstructor
@Data
public class EventDto  {

    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location;
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;

}
//    Event 생성 API 구현: 입력값 제한하기
//
//        입력값 제한
//        id 또는 입력 받은 데이터로 계산해야 하는 값들은 입력을 받지 않아야 한다.
//        EventDto 적용
//
//        DTO -> 도메인 객체로 값 복사
//        ModelMapper
//<dependency>
//<groupId>org.modelmapper</groupId>
//<artifactId>modelmapper</artifactId>
//<version>2.3.1</version>
//</dependency>
//
//        통합 테스트로 전환
//@WebMvcTest 빼고 다음 애노테이션 추가
//@SpringBootTest
//@AutoConfigureMockMvc
//Repository @MockBean 코드 제거
//
//        테스트 할 것
//        입력값으로 누가 id나 eventStatus, offline, free 이런 데이터까지 같이 주면?
//        Bad_Request로 응답 vs 받기로 한 값 이외는 무시

