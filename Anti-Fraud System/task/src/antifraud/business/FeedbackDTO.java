package antifraud.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedbackDTO {
    @Min(value = 0, message = "Incorrect id!")
    @NonNull
    private Long transactionId;
    @EnumValueCorrect(enumClazz = TransactionStatus.class, message = "Incorrect feedback!")
    private String feedback;
}