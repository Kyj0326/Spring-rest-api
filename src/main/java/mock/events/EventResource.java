package mock.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

//ResourceSupport is now RepresentationModel
//
//        Resource is now EntityModel
//
//        Resources is now CollectionModel
//
//        PagedResources is now PagedModel
public class EventResource extends EntityModel<Event> {
    public EventResource(Event event, Link... links) {
        super(event, links);
        //add(new Link("http://localhost:8080/api/events/"+event.getId())); 위아래는 같은것인데, 타입 세이프하게 아래처럼!! api/events를바꾸면 아래는 안바꿔도됨~
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}

//우리는 EventResource로 리턴을 했고, 이거를 오브젝트매퍼가 시리얼라이제이션 할때!
//빈시리얼라이저를 쓴다! BeanSerializer 이거는 기본적으로 필드 이름을 사용한다. event가 다른 필드들을 가지고있는 컴포짓 객체이기때문에
//event이름 아래에다가 다 넣어주는거임
//근데 event로 감싸지않고 그냥 넣고싶다!
//@JsonUnwrapped를 쓰면 꺼내준다!
//public class EventResource extends RepresentationModel{
//
//    @JsonUnwrapped
//    private Event event;
//
//    public EventResource(Event event){
//        this.event=event;
//    }
//
//    public Event getEvent(){
//        return event;
//    }
//
//}
