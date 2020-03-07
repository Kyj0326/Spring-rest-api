package mock.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
public class EventController {

    @Autowired
    private EventRepository repository;


    @PostMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity createEvent(@RequestBody Event event){
        Event newEvent = repository.save(event);
        URI createdUri =  linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdUri).body(event);
    }


}
