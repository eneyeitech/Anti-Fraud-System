package antifraud.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResultDTO {
    private String result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String info;
}
