package me.manylove.restapi.events;

import me.manylove.restapi.common.ErrorsEntityModel;
import me.manylove.restapi.index.IndexController;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE )
public class EventContoller {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @Autowired
    public EventContoller(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@Validated @RequestBody EventDto eventDto, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }
        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()){
//            return ResponseEntity.badRequest().build();
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = this.eventRepository.save(event);


        WebMvcLinkBuilder selLinkBuilder =  linkTo(EventContoller.class).slash(newEvent.getId());
        URI createUri = selLinkBuilder.toUri();
        EventEntitiyModel eventEntityModel = new EventEntitiyModel(newEvent);
//        EntityModel<Event> eventEntityModel = EntityModel.of(newEvent);
        eventEntityModel.add(linkTo(EventContoller.class).withRel("query-events"));
        eventEntityModel.add(selLinkBuilder.withRel("update-event"));
        eventEntityModel.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(createUri).body(eventEntityModel);
    }

//    private ResponseEntity<ErrorsEntityModel> badRequest(Errors errors) {
//        return ResponseEntity.badRequest().body(new ErrorsEntityModel(errors));
//    }

//    private ResponseEntity<EntityModel<Errors>> badRequest(Errors errors) {
//        EntityModel<Errors> eventEntityModel = EntityModel.of(errors);
//        eventEntityModel.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
//        return ResponseEntity.badRequest().body(eventEntityModel);
//    }
}
