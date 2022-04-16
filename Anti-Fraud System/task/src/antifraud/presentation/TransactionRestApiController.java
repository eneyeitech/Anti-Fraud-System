package antifraud.presentation;

import antifraud.business.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TransactionRestApiController {
    private final RepositoryService repository;
    private final TransactionStatusResolver statusResolver;
    private final EntityMapper<TransactionDTO, TransactionEntity> transactionMapper;

    @Autowired
    public TransactionRestApiController(RepositoryService repository,
                                        TransactionStatusResolver statusResolver,
                                        EntityMapper<TransactionDTO, TransactionEntity> transactionMapper) {

        this.repository = repository;
        this.statusResolver = statusResolver;
        this.transactionMapper = transactionMapper;
    }

    @PostMapping("/api/antifraud/transaction")
    public ResultDTO acceptTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        ResultDTO result = statusResolver.resolve(transactionDTO);
        logTransaction(transactionDTO, TransactionStatus.valueOf(result.getResult()));
        return result;
    }

    private void logTransaction(TransactionDTO transactionDTO, TransactionStatus status) {
        TransactionEntity transactionEntity = transactionMapper.toEntity(transactionDTO);
        transactionEntity.setStatus(status);
        repository.saveTransactionToHistory(transactionEntity);
    }
}
