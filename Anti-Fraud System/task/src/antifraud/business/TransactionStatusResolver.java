package antifraud.business;

import antifraud.exception.BadAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

@Component
public class TransactionStatusResolver {
    private final RepositoryService repository;
    private static final String AMOUNT_IS_INCORRECT = "amount";
    private static final String CARD_IS_STOLEN = "card-number";
    private static final String IP_IS_BLOCKED = "ip";
    private static final String ALL_IS_CORRECT = "none";

    @Autowired
    public TransactionStatusResolver(RepositoryService repository) {
        this.repository = repository;
    }

    public ResultDTO resolve(TransactionDTO transaction) {
        TransactionStatus status = retrieveTransactionStatus(transaction.getAmount());
        Set<String> info = new TreeSet<>();
        if (status != TransactionStatus.ALLOWED) {
            info.add(AMOUNT_IS_INCORRECT);
        }
        if (repository.isCardStolen(transaction.getNumber())) {
            clearInfoIfThereMoreSevereStatus(status, info);
            status = TransactionStatus.PROHIBITED;
            info.add(CARD_IS_STOLEN);
        }
        if (repository.isIpBanned(transaction.getIp())) {
            clearInfoIfThereMoreSevereStatus(status, info);
            status = TransactionStatus.PROHIBITED;
            info.add(IP_IS_BLOCKED);
        }
        if (status == TransactionStatus.ALLOWED) {
            info.add(ALL_IS_CORRECT);
        }
        return new ResultDTO(status.name(), String.join(", ", info));
    }

    private void clearInfoIfThereMoreSevereStatus(TransactionStatus status, Set<String> info) {
        if (status == TransactionStatus.MANUAL_PROCESSING) {
            info.clear();
        }
    }

    public TransactionStatus retrieveTransactionStatus(long amount) {
        return Arrays.stream(TransactionStatus.values())
                .filter(val -> Math.max(val.getMin(), amount) == Math.min(amount, val.getMax()))
                .findFirst()
                .orElseThrow(BadAmountException::new);
    }
}
