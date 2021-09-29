package me.manylove.restapi.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class EventContollerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {

        EventDto event = EventDto.builder()
                .name("Spring")
                .description("Rest API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 9, 29, 14, 47 ))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 10, 29, 14, 47))
                .beginEventDateTime(LocalDateTime.of(2021, 9, 30, 14, 47))
                .endEventDateTime(LocalDateTime.of(2021, 10, 30, 14, 47))
                .basePrice(0)
                .maxPrice(0)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("id").exists(),
                        header().exists(HttpHeaders.LOCATION),
                        header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE),
                        jsonPath("free").value(true),
                        jsonPath("offline").value(true),
                        jsonPath("eventStatus").value(EventStatus.DRAFT.name())
                );
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("id").exists())
//                .andExpect(header().exists(HttpHeaders.LOCATION))
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
//                .andExpect(jsonPath("free").value(true))
//                .andExpect(jsonPath("offline").value(true))
//                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
    }

//    @Test
//    @DisplayName("Event 생성 API 확인")
//    public void createEventDto() throws Exception {
//
//        EventDto event = EventDto.builder()
//                .name("Spring")
//                .description("Rest API Development with Spring")
//                .beginEventDateTime(LocalDateTime.of(2021, 9, 28, 14, 47 ))
//                .closeEnrollmentDateTime(LocalDateTime.of(2021, 9, 28, 14, 47))
//                .beginEventDateTime(LocalDateTime.of(2021, 9, 28, 14, 47))
//                .endEventDateTime(LocalDateTime.of(2021, 9, 28, 14, 47))
//                .basePrice(100)
//                .maxPrice(200)
//                .limitOfEnrollment(100)
//                .location("강남역 D2 스타텁 팩토리")
//                .build();
//
//        mockMvc.perform(post("/api/events")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaTypes.HAL_JSON)
//                        .content(objectMapper.writeValueAsString(event)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("id").exists())
//                .andExpect(header().exists(HttpHeaders.LOCATION))
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
//                .andExpect(jsonPath("id").value(Matchers.not(100)))
//                .andExpect(jsonPath("free").value(Matchers.not(true)))
//                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
//    }

//    @Test
//    @DisplayName("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
//    public void createEventError() throws Exception {
//
//        Event event = Event.builder()
//                .id(100)
//                .name("Spring")
//                .description("Rest API Development with Spring")
//                .beginEventDateTime(LocalDateTime.of(2021, 9, 28, 14, 47 ))
//                .closeEnrollmentDateTime(LocalDateTime.of(2021, 9, 28, 14, 47))
//                .beginEventDateTime(LocalDateTime.of(2021, 9, 28, 14, 47))
//                .endEventDateTime(LocalDateTime.of(2021, 9, 28, 14, 47))
//                .basePrice(100)
//                .maxPrice(200)
//                .limitOfEnrollment(100)
//                .location("강남역 D2 스타텁 팩토리")
//                .free(true)
//                .offLine(false)
//                .build();
//
//        mockMvc.perform(post("/api/events")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaTypes.HAL_JSON)
//                        .content(objectMapper.writeValueAsString(event)))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }

    @Test
    @DisplayName("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void create_event_bad_request_empty_input() throws Exception {
        EventDto event = EventDto.builder()
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void create_event_bad_request_Wrong_input2() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("Rest API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 9, 30, 14, 47 ))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 9, 29, 14, 47))
                .beginEventDateTime(LocalDateTime.of(2021, 9, 29, 14, 47))
                .endEventDateTime(LocalDateTime.of(2021, 9, 28, 14, 47))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
//                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
        ;
    }
}