package mock.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("test")
                .description("Spring Rest API").build();

        assertThat(event).isNotNull();

    }

    @Test
    public void javaBean(){
        //Given
        String spring = "Spring";
        String rest_api = "Rest API";

        //when
        Event event = new Event();
        event.setName(spring);
        event.setDescription(rest_api);

        //then
        assertThat(event.getName()).isEqualTo(spring);
        assertThat(event.getDescription()).isEqualTo(rest_api);

    }
    @Test
    public void testFree() {
        // Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isTrue();

        // Given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isFalse();

        // Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void testOffline() {
        // Given
        Event event = Event.builder()
                .location("감남역 네이버 D2 스타텁 팩토리")
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isTrue();

        // Given
        event = Event.builder()
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isFalse();
    }
    
}