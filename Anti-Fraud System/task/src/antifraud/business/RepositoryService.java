package antifraud.business;

import antifraud.exception.*;
import antifraud.persistence.StolenCreditCardRepository;
import antifraud.persistence.SuspiciousIPRepository;
import antifraud.persistence.UserRepository;
import antifraud.utility.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryService {
    private final UserRepository userRepository;
    private final SuspiciousIPRepository ipRepository;
    private final StolenCreditCardRepository stolenCardRepository;

    @Autowired
    public RepositoryService(UserRepository userRepository,
                             SuspiciousIPRepository ipRepository,
                             StolenCreditCardRepository stolenCardRepository) {
        this.userRepository = userRepository;
        this.ipRepository = ipRepository;
        this.stolenCardRepository = stolenCardRepository;
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

    public SuspiciousIPEntity create(SuspiciousIPEntity ipEntity) {
        if (isIpBanned(ipEntity.getIp())) {
            throw new IpBannedException();
        }
        return ipRepository.save(ipEntity);
    }

    public boolean isIpBanned(String ip) {
        return ipRepository.existsByIp(ip);
    }

    public void deleteSuspiciousIp(String ip) {
        if (!isIpBanned(ip)) {
            throw new IpNotFoundException();
        }
        ipRepository.delete(ipRepository.findFirstByIp(ip));
    }

    public List<SuspiciousIPEntity> getAllSuspiciousIps() {
        return (List<SuspiciousIPEntity>) ipRepository.findAll();
    }

    public StolenCreditCardEntity create(StolenCreditCardEntity cardEntity) {
        if (isCardStolen(cardEntity.getNumber())) {
            throw new CardIsStolenException();
        }
        return stolenCardRepository.save(cardEntity);
    }

    public boolean isCardStolen(String number) {
        return stolenCardRepository.existsByNumber(number);
    }

    public void deleteStolenCreditCard(String cardNumber) {
        if (!isCardStolen(cardNumber)) {
            throw new CardNotFoundException();
        }
        stolenCardRepository.delete(stolenCardRepository.findFirstByNumber(cardNumber));
    }

    public List<StolenCreditCardEntity> getAllStolenCards() {
        return (List<StolenCreditCardEntity>) stolenCardRepository.findAll();
    }
}