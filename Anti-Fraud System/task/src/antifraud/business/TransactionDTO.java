package antifraud.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.LuhnCheck;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDTO {
    @Min(value = 1, message = "Wrong amount!")
    private long amount;
    @Pattern(regexp = "((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)(\\.(?!$)|$)){4}", message = "Wrong ip format!")
    private String ip;
    @LuhnCheck(message = "Incorrect card number!")
    private String number;
}
