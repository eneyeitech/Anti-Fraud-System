package antifraud.presentation;

import antifraud.business.*;
import antifraud.exception.FeedbackAlreadyAcceptedException;
import antifraud.exception.SameLimitChangeException;
import org.hibernate.validator.constraints.LuhnCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
public class TransactionRestApiController {
    private final RepositoryService repository;
    private final TransactionStatusResolver statusResolver;
    private final Converter<TransactionEntity, TransactionDTO> transactionConverter;
    private final StatusLimitProcessor limitChangerProcessor;

    @Autowired
    public TransactionRestApiController(RepositoryService repository,
                                        TransactionStatusResolver statusResolver,
                                        Converter<TransactionEntity, TransactionDTO> transactionConverter,
                                        StatusLimitProcessor limitChangerProcessor) {
        this.repository = repository;
        this.statusResolver = statusResolver;
        this.transactionConverter = transactionConverter;
        this.limitChangerProcessor = limitChangerProcessor;
    }

    @PostMapping("/api/antifraud/transaction")
    public ResultDTO acceptTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        ResultDTO result = statusResolver.resolve(transactionDTO);
        logTransaction(transactionDTO, TransactionStatus.valueOf(result.getResult()));
        return result;
    }

    @PutMapping("/api/antifraud/transaction")
    public TransactionDTO makeFeedbackForTransaction(@RequestBody @Valid FeedbackDTO feedbackDTO) {
        TransactionEntity transactionEntity = repository.getTransaction(feedbackDTO.getTransactionId());
        TransactionStatus newStatus = TransactionStatus.valueOf(feedbackDTO.getFeedback());
        TransactionStatus transactionStatus = transactionEntity.getStatus();
        System.out.println((transactionEntity));
        System.out.println((newStatus));
        System.out.println((transactionStatus));
        if (transactionEntity.getFeedback() != null) {
            throw new FeedbackAlreadyAcceptedException();
        }
        if (transactionStatus == newStatus) {
            throw new SameLimitChangeException();
        }
        transactionEntity.setFeedback(newStatus);
        limitChangerProcessor.recalculateLimits(transactionStatus, newStatus, transactionEntity.getAmount());
        repository.saveTransactionToHistory(transactionEntity);
        return transactionConverter.toDTO(transactionEntity);
    }

    @GetMapping("/api/antifraud/history")
    public List<TransactionDTO> showTransactionHistory() {
        return repository.getAllTransactionsFromHistory().stream()
                .map(transactionConverter::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/antifraud/history/{number}")
    public List<TransactionDTO> getTransactionFromHistoryByCardNumber(@PathVariable @Valid @LuhnCheck
                                                                              String number) {
        return repository.getTransactionsByCardNumber(number).stream()
                .map(transactionConverter::toDTO)
                .collect(Collectors.toList());
    }

    private void logTransaction(TransactionDTO transactionDTO, TransactionStatus status) {
        TransactionEntity transactionEntity = transactionConverter.toEntity(transactionDTO);
        transactionEntity.setStatus(status);
        repository.saveTransactionToHistory(transactionEntity);
    }
}