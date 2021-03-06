package mock.events;

import mock.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    @Autowired
    private EventRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EventValidator validator;

//허나 모델메퍼를 사용하면 Reflection이 발생하긴 한다.
//    리플렉션(Reflection)이란?
//    자바에서 제공하는 리플렉션(Reflection)은 C, C++과 같은 언어를 비롯한 다른 언어에서는 볼 수 없는 기능입니다. 이미 로딩이 완료된 클래스에서 또 다른 클래스를 동적으로 로딩(Dynamic Loading)하여 생성자(Constructor), 멤버 필드(Member Variables) 그리고 멤버 메서드(Member Method) 등을 사용할 수 있도록 합니다.
//
//            그러니까, 컴파일 시간(Compile Time)이 아니라 실행 시간(Run Time)에 동적으로 특정 클래스의 정보를 객체화를 통해 분석 및 추출해낼 수 있는 프로그래밍 기법이라고 표현할 수 있습니다.
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){
        if(errors.hasErrors()){
            return badRequest(errors);
        }

        validator.validate(eventDto, errors);
        if(errors.hasErrors()){
            return badRequest(errors);
        }
//        Event event = Event.builder()     ModelMapper가 없으면 이것처럼 다 날코딩 해야 한다.
//                .name(eventDto.getName())
//                .description(eventDto.getDescription())
//                .build();
        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = repository.save(event);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri =  selfLinkBuilder.toUri();
        EventResource eventResouce = new EventResource(event);
        eventResouce.add(linkTo(EventController.class).withRel("query-events"));
        eventResouce.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));

        eventResouce.add(selfLinkBuilder.withRel("update-events"));
        return ResponseEntity.created(createdUri).body(eventResouce);
    }


    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler){
        Page<Event> page = repository.findAll(pageable);
        PagedModel<EntityModel<Event>> pageResource = assembler.toModel(page, e-> new EventResource(e));
        pageResource.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(pageResource);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id){
        Optional<Event> res = repository.findById(id);
        if(res.isPresent()==false){
            return ResponseEntity.notFound().build();
        }
        Event event = res.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));
        return ResponseEntity.ok(eventResource) ;

    }

    @PutMapping("{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id, @RequestBody @Valid EventDto eventDto, Errors errors){

        Optional<Event> optionalEvent = repository.findById(id);

        if(optionalEvent.isPresent()==false){
            return ResponseEntity.notFound().build();
        }

        if(errors.hasErrors()){
            return badRequest(errors);
        }

        validator.validate(eventDto,errors);
        if(errors.hasErrors()){
            return badRequest(errors);
        }

        Event oldEvent = optionalEvent.get();
        modelMapper.map(eventDto, oldEvent);
        Event savedEvent = repository.save(oldEvent);

        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(new Link("/docs/index.html#resources-events-update").withRel("profile"));

        return ResponseEntity.ok(eventResource);


    }



    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }


}
