package mock.common;

import mock.events.EventDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() >0 ){
            errors.rejectValue("basePrice","wrongvValue","BasePrice is Wrong.");
            errors.rejectValue("maxPrice","wrongvValue","MaxPrice is Wrong.");
        }

        //~~기타등등 만들어봐라
    }

}
