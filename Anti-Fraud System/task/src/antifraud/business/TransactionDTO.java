package antifraud.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.LuhnCheck;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDTO {
    @Min(value = 1, message = "Wrong amount!")
    private long amount;
    @Pattern(regexp = Regexes.IP_FORMAT, message = "Wrong ip format!")
    private String ip;
    @LuhnCheck(message = "Incorrect card number!")
    private String number;
    @EnumValueCorrect(enumClazz = WorldRegion.class, message = "Incorrect region!")
    private String region;
    private LocalDateTime date;
}
