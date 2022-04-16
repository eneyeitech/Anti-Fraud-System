package antifraud.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuspiciousIpDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long id;
    @NonNull
    @Pattern(regexp = Regexes.IP_FORMAT, message = "Wrong ip format!")
    private String ip;
}
