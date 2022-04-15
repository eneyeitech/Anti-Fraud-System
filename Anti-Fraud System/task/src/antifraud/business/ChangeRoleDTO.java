package antifraud.business;

import antifraud.utility.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
public class ChangeRoleDTO {
    @NotEmpty
    private String username;
    @RoleCorrect(enumClazz = Role.class, message = "Role incorrect")
    private String role;
}
