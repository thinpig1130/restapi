package me.manylove.restapi.events;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    @DisplayName("빌더 작동 테스트")
    void builder(){
        Event event = Event.builder()
                .name("REST API 강좌")
                .description("온전한 REST API 개발을 위한 구현 예제가 담긴 강좌")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    @DisplayName("JAVA Bean Spec 테스트")
    void javaBean(){
        // Given
        String name = "Event";
        String description = "Spring";

        // When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    @DisplayName("Free 자동입력 테스트")
    void testFree(){
        // Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        // When
        event.update();

        // Then
        assertTrue(event.isFree());

        // Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        // When
        event.update();

        // Then
        assertFalse(event.isFree());

    }

    @Test
    @DisplayName("Offline 자동입력 테스트")
    void testOffline(){
        // Given
        Event event = Event.builder()
                .location("여기")
                .build();
        // When
        event.update();

        // Then
        assertTrue(event.isOffline());

        // Given
        event = Event.builder()
                .build();
        // When
        event.update();

        // Then
        assertFalse(event.isOffline());

    }

}