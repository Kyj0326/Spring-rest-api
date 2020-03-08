package mock.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent // ObjectMapper에 등록하려면 이 에노테이션이면 된다. 오브젝트 매퍼는 이거를 errors라는 객체를 serialize할때 이 빈을 사용한다.
public class ErrorsSerializer extends JsonSerializer<Errors> {


    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray(); // erros안에 에러개니까 그걸 배열로 담아주기 위해서!
        errors.getFieldErrors().stream().forEach(e->{

            try {
                gen.writeStartObject();//json오브젝틀를 만들고 닫는다
                    gen.writeStringField("field",e.getField());
                    gen.writeStringField("objectName",e.getObjectName());
                    gen.writeStringField("code",e.getCode());
                    gen.writeStringField("defaultMessage",e.getDefaultMessage());
                    Object rejectedValue = e.getRejectedValue();
                    if ( rejectedValue != null){
                        gen.writeStringField("rejectedValue", rejectedValue.toString());
                    }
                gen.writeEndObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        errors.getGlobalErrors().stream().forEach(e->{

            try {
                gen.writeStartObject();//json오브젝틀를 만들고 닫는다
                gen.writeStringField("objectName",e.getObjectName());
                gen.writeStringField("code",e.getCode());
                gen.writeStringField("defaultMessage",e.getDefaultMessage());
                gen.writeEndObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            }



        });

        gen.writeEndArray();
    }
}
