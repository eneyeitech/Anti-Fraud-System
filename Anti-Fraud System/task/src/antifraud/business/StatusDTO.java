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

    public static class UserLocked extends StatusDTO {
        public UserLocked(String username) {
            super(String.format("User %s locked!", username));
        }
    }

    public static class UserUnlocked extends StatusDTO {
        public UserUnlocked(String username) {
            super(String.format("User %s unlocked!", username));
        }
    }

    public static class IpRemoved extends StatusDTO {
        public IpRemoved(String ip) {
            super(String.format("IP %s successfully removed!", ip));
        }
    }

    public static class CreditCardRemoved extends StatusDTO {
        public CreditCardRemoved(String number) {
            super(String.format("Card %s successfully removed!", number));
        }
    }
}
