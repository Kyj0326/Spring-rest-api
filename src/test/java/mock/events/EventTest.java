package mock.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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
//    @Parameters({
//            "0,0,true",
//            "100,0,false",
//            "0,100,false"
//    })
//    @Parameters(method = "paramsForTestFree") 이렇게 해도되고
    @Parameters //아래 컨벤션 parametersForTestFree 으로하면 명시안해도됨
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private Object[] parametersForTestFree(){
        return new Object[]{
                new Object[] {0,0,true},
                new Object[] {100,0,false},
                new Object[] {0,100,false},
                new Object[] {100,200,false},
        };
    }

    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline) {
        // Given
        Event event = Event.builder()
                .location(location)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(isOffline);

    }

    private Object[] parametersForTestOffline(){
        return new Object[] {
                new Object[] {"강남",true},
                new Object[] {"",false}
        };
    }
}