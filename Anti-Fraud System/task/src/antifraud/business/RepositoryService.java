package antifraud.business;

import antifraud.exception.UserExistsException;
import antifraud.exception.UserNotFound;
import antifraud.persistence.UserRepository;
import antifraud.utility.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryService {
    UserRepository userRepository;

    @Autowired
    public RepositoryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity create(UserEntity userEntity) {
        if (userRepository.existsByUsernameIgnoreCase(userEntity.getUsername())) {
            throw new UserExistsException();
        }
        userEntity.setRole(countUsers() == 0 ? Role.ADMINISTRATOR : Role.MERCHANT);
        userEntity.setLocked(userEntity.getRole() != Role.ADMINISTRATOR);
        return userRepository.save(userEntity);
    }

    public List<UserEntity> getAllUsers() {
        return (List<UserEntity>) userRepository.findAll();
    }

    public void delete(String username) {
        userRepository.delete(findUser(username));
    }

    public long countUsers() {
        return userRepository.count();
    }

    public UserEntity findUser(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(UserNotFound::new);
    }

    public void update(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}
