package mock.events;

import mock.events.EventDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() >0 ){
//            errors.rejectValue("basePrice","wrongvValue","BasePrice is Wrong.");
//            errors.rejectValue("maxPrice","wrongvValue","MaxPrice is Wrong.");
            errors.reject("wrongPrice","Values to prices are wrong"); //reject는 globalerror에 들어감

        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())||
            endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())||
              endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())){
            errors.rejectValue("endEventDateTime", " WrongValue","");// rejectvalue는 fielderror에 들어감
        }

        //~~기타등등 만들어봐라
    }

}
