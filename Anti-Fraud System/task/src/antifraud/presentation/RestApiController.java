package antifraud.presentation;

import antifraud.business.*;
import antifraud.exception.BadAmountException;
import antifraud.business.UserEntity;
import antifraud.exception.BadRequestException;
import antifraud.exception.RoleIsAlreadyProvided;
import antifraud.utility.Operation;
import antifraud.utility.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RestApiController {
    private final RepositoryService repository;
    private final Converter<UserEntity, UserDTO> userConverter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RestApiController(RepositoryService repository,
                             Converter<UserEntity, UserDTO> userConverter,
                             PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/antifraud/transaction")
    public ResultDTO acceptTransaction(@RequestBody AmountDTO amountDTO) {
        return new ResultDTO(retrieveTransactionStatus(amountDTO.getAmount()).name());
    }

    @PostMapping("/api/auth/user")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntity newUser = repository.create(userConverter.toEntity(userDTO));
        return new ResponseEntity<>(userConverter.toDTO(newUser), HttpStatus.CREATED);
    }

    @GetMapping("/api/auth/list")
    public List<UserDTO> getAllUsers() {
        return repository.getAllUsers().stream()
                .map(userConverter::toDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/api/auth/user/{username}")
    public StatusDTO deleteUser(@PathVariable String username) {
        repository.delete(username);
        return new StatusDTO(username, "Deleted successfully!");
    }

    @PutMapping("/api/auth/role")
    public UserDTO updateUserRole(@RequestBody @Valid ChangeRoleDTO changeRoleDTO) {
        UserEntity userEntity = repository.findUser(changeRoleDTO.getUsername());
        Role newRole = Role.valueOf(changeRoleDTO.getRole());
        if (!List.of(Role.MERCHANT, Role.SUPPORT).contains(newRole)) {
            throw new BadRequestException("Operation not allowed!");
        }
        if (userEntity.getRole() == newRole) {
            throw new RoleIsAlreadyProvided();
        }
        userEntity.setRole(newRole);
        repository.update(userEntity);
        return userConverter.toDTO(userEntity);
    }

    @PutMapping("/api/auth/access")
    public StatusDTO lockUnlockUser(@RequestBody @Valid UserOperationDTO operationDTO) {
        String username = operationDTO.getUsername();
        UserEntity userEntity = repository.findUser(username);
        if (userEntity.getRole() == Role.ADMINISTRATOR) {
            throw new BadRequestException("Operation not allowed!");
        }
        Operation operation = Operation.valueOf(operationDTO.getOperation());
        userEntity.setLocked(operation == Operation.LOCK);
        repository.update(userEntity);
        return operation == Operation.LOCK ? new StatusDTO.Locked(username) : new StatusDTO.Unlocked(username);
    }

    public TransactionStatus retrieveTransactionStatus(long amount) {
        return Arrays.stream(TransactionStatus.values())
                .filter(val -> Math.max(val.getMin(), amount) == Math.min(amount, val.getMax()))
                .findFirst()
                .orElseThrow(BadAmountException::new);
    }
}
