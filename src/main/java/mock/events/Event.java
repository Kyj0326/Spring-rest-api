package mock.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @EqualsAndHashCode ( of ="id")
@Getter @Setter @NoArgsConstructor
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;

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
    private boolean offline;
    private boolean free;
    @Enumerated (EnumType.STRING)
    private EventStatus eventStatus;

    
}
//● 왜 @EqualsAndHasCode에서 of를 사용하는가
// 나중에 객체간에 연관관계가 있을 떄 서로 참조하는 경우가 발생하면 stack overflow가 발생할 수 있다.
//그래서 of를사용해라!
//Book 에 Writer가 있고 Writer에서 List<Book> 을 가지고 있다고 가정했을 때 Book과 Writer가 서로를 참조하니까 상호참조라고 할 수 있습니다. 이 경우에 @Data 애노테이션이 만들어 주는 Book의 equals를 사용하면 Book -> Writer -> Book 무한 반복으로 스택오버플로가 발생하는 것을 볼 수 있습니다. 그걸 방지하려고 equals를 만들 때 id만 쓰도록 설정한겁니다.

//        ● 왜 @Builder를 사용할 때 @AllArgsConstructor가 필요한가
// @Builder는 모든 변수를 포함한 디폴트 생성자만 만들어 주기때문에 다른 패키지에서 호출하기가 애매하다
//그래서 필요하다.

//        ● @Data를 쓰지 않는 이유
//거기에 @EqualsAndHasCode를 다 써서 구현하기때문에


//        ● 애노테이션 줄일 수 없나
// Lombok은 아직 그게 안된다 불가능하다!