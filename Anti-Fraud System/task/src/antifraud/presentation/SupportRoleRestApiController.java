package antifraud.presentation;

import antifraud.business.*;
import org.hibernate.validator.constraints.LuhnCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
public class SupportRoleRestApiController {
    private final RepositoryService repository;
    private final Converter<StolenCreditCardEntity, StolenCreditCardDTO> cardConverter;
    private final Converter<SuspiciousIPEntity, SuspiciousIpDTO> ipConverter;

    @Autowired
    public SupportRoleRestApiController(RepositoryService repository,
                                        Converter<StolenCreditCardEntity, StolenCreditCardDTO> cardConverter,
                                        Converter<SuspiciousIPEntity, SuspiciousIpDTO> ipConverter) {
        this.repository = repository;
        this.cardConverter = cardConverter;
        this.ipConverter = ipConverter;
    }

    @PostMapping("/api/antifraud/suspicious-ip")
    public ResponseEntity<SuspiciousIpDTO> addSuspiciousIP(@RequestBody @Valid SuspiciousIpDTO ipDTO) {
        SuspiciousIPEntity ipEntity = repository.create(ipConverter.toEntity(ipDTO));
        ipDTO.setId(ipEntity.getId());
        return ResponseEntity.ok(ipDTO);
    }

    @DeleteMapping("/api/antifraud/suspicious-ip/{ip}")
    public StatusDTO.IpRemoved deleteSuspiciousIP(@PathVariable @Valid
                                                  @Pattern(regexp = Regexes.IP_FORMAT,
                                                          message = "Wrong ip format!")
                                                          String ip) {
        repository.deleteSuspiciousIp(ip);
        return new StatusDTO.IpRemoved(ip);
    }

    @GetMapping("/api/antifraud/suspicious-ip")
    public List<SuspiciousIpDTO> showAllSuspiciousIPs() {
        return repository.getAllSuspiciousIps().stream()
                .map(ipConverter::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/antifraud/stolencard")
    public ResponseEntity<StolenCreditCardDTO> addStolenCard(@RequestBody @Valid StolenCreditCardDTO cardDTO) {
        StolenCreditCardEntity cardEntity = repository.create(cardConverter.toEntity(cardDTO));
        cardDTO.setId(cardEntity.getId());
        return ResponseEntity.ok(cardDTO);
    }

    @DeleteMapping("/api/antifraud/stolencard/{number}")
    public StatusDTO.CreditCardRemoved deleteStolenCard(@PathVariable @Valid @LuhnCheck String number) {
        repository.deleteStolenCreditCard(number);
        return new StatusDTO.CreditCardRemoved(number);
    }

    @GetMapping("/api/antifraud/stolencard")
    public List<StolenCreditCardDTO> showAllStolenCards() {
        return repository.getAllStolenCards().stream()
                .map(cardConverter::toDTO)
                .collect(Collectors.toList());
    }
}