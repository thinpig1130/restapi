package me.manylove.restapi.events;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @DisplayName("free 속성 갱신 테스트 : (basePrice, maxPrice) -> isFree()")
    @ParameterizedTest(name="{index} ({0}, {1}) -> {2}")
    @CsvSource({
            "0, 0, true",
            "100, 0, falae",
            "0, 1000, falae"
    })
    void testFree(int basePrice, int maxPrice, boolean isFree){
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        // When
        event.update();

        // Then
        assertEquals(event.isFree(), isFree);
    }


    @DisplayName("Offline 속성 갱신 테스트 : (location) -> isOffline()")
    @ParameterizedTest(name="{index} ({0}) -> {2}")
    @CsvSource({
            "'여기', true",
            "'', false",
            "' ', false",
            ", false"
    })
    void testOffline(String location, Boolean isOffline){
        // Given
        Event event = Event.builder()
                .location(location)
                .build();
        // When
        event.update();

        // Then
        assertEquals(event.isOffline(), isOffline);
    }

}