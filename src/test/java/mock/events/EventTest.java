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

}