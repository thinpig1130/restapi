package me.manylove.restapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.hateoas.server.mvc.ControllerLinkRelationProvider;
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
        EventResource eventEntityModel = new EventResource(newEvent);
//        EntityModel<Event> eventEntityModel = EntityModel.of(newEvent);
        eventEntityModel.add(linkTo(EventContoller.class).withRel("query-events"));
        eventEntityModel.add(selLinkBuilder.withRel("update-event"));
        eventEntityModel.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(createUri).body(eventEntityModel);
    }
}
