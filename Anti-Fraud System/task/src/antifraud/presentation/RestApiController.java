package antifraud.presentation;

import antifraud.business.*;
import antifraud.exception.BadAmountException;
import antifraud.persistence.UserEntity;
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

    public TransactionStatus retrieveTransactionStatus(long amount) {
        return Arrays.stream(TransactionStatus.values())
                .filter(val -> Math.max(val.getMin(), amount) == Math.min(amount, val.getMax()))
                .findFirst()
                .orElseThrow(BadAmountException::new);
    }
}
