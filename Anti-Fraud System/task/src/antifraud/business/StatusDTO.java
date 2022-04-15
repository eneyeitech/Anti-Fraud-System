package antifraud.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class StatusDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;
    @NonNull
    private String status;

    public static class Locked extends StatusDTO {
        public Locked(String username) {
            super(String.format("User %s locked!", username));
        }
    }

    public static class Unlocked extends StatusDTO {
        public Unlocked(String username) {
            super(String.format("User %s unlocked!", username));
        }
    }

}
