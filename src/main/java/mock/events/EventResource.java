package mock.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

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
