package me.manylove.restapi.common;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.manylove.restapi.events.Event;
import me.manylove.restapi.events.EventContoller;
import me.manylove.restapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsEntityModel extends RepresentationModel {


    public Errors errors;

    public ErrorsEntityModel(Errors errors) {
        this.errors = errors;
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
//        add(linkTo(EventContoller.class).slash(1).withSelfRel());
    }

//    public Errors getErrors() {
//        return errors;
//    }
}

//public class ErrorsEntityModel extends EntityModel<Errors> {
//
//
//
//    public ErrorsEntityModel(Errors content, Link... links) {
//        super(content, links);
//        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
//    }
//
//}
