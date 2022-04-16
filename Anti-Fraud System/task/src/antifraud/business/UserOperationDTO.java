package antifraud.business;

import antifraud.utility.Operation;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
public class UserOperationDTO {
    @NotEmpty
    private String username;
    @EnumValueCorrect(enumClazz = Operation.class, message = "Operation must be LOCK or UNLOCK!")
    private String operation;
}