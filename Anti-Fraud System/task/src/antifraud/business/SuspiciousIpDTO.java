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
    @Pattern(regexp = "((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)(\\.(?!$)|$)){4}", message = "Wrong IP format!")
    private String ip;
}
