package me.manylove.restapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventResource extends RepresentationModel {
    @JsonUnwrapped
    Event event;

    public EventResource(Event event) {
         this.event = event;
         add(linkTo(EventContoller.class).slash(event.getId()).withSelfRel());
    }
}
