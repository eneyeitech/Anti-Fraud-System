package antifraud.business;

import antifraud.exception.BadAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

@Component
public class TransactionStatusResolver {
    private final RepositoryService repository;
    private final EntityMapper<TransactionDTO, TransactionEntity> transactionMapper;
    private final int CHECK_HOURS_BEFORE_TRANSACTION = 1;
    private static final String AMOUNT_IS_INCORRECT = "amount";
    private static final String CARD_IS_STOLEN = "card-number";
    private static final String IP_IS_BLOCKED = "ip";
    private static final String ALL_IS_CORRECT = "none";
    private static final String SUSPICIOUS_IP_OPERATION = "ip-correlation";
    private static final String SUSPICIOUS_REGION_OPERATION = "region-correlation";

    @Autowired
    public TransactionStatusResolver(RepositoryService repository,
                                     EntityMapper<TransactionDTO, TransactionEntity> transactionMapper) {
        this.repository = repository;
        this.transactionMapper = transactionMapper;
    }

    public ResultDTO resolve(TransactionDTO transactionDTO) {
        TransactionEntity transaction = transactionMapper.toEntity(transactionDTO);
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
        TransactionStatus regionViolationStatus = getStatusIfSameCardFromDifferentRegions(transaction);
        if (regionViolationStatus != TransactionStatus.ALLOWED) {
            status = regionViolationStatus;
            info.add(SUSPICIOUS_REGION_OPERATION);
        }
        TransactionStatus ipViolationStatus = getStatusIfSameCardFromDifferentIPs(transaction);
        if (ipViolationStatus != TransactionStatus.ALLOWED) {
            status = ipViolationStatus;
            info.add(SUSPICIOUS_IP_OPERATION);
        }
        if (status == TransactionStatus.ALLOWED) {
            info.add(ALL_IS_CORRECT);
        }
        System.out.println(status);
        return new ResultDTO(status.name(), String.join(", ", info));
    }

    private TransactionStatus getStatusIfSameCardFromDifferentRegions(TransactionEntity transaction) {
        List<TransactionEntity> transactionsOfLastHour =
                repository.getTransactionsOfCardFromOtherRegionInTimeInterval(
                        transaction.getNumber(),
                        transaction.getRegion(),
                        transaction.getDate().minusHours(CHECK_HOURS_BEFORE_TRANSACTION),
                        transaction.getDate()
                );
        long uniqueByRegionCount = countDistinctByField(transactionsOfLastHour, TransactionEntity::getRegion);
        return getStatusBasedOnNumberOfTransactions(uniqueByRegionCount);
    }

    private TransactionStatus getStatusIfSameCardFromDifferentIPs(TransactionEntity transaction) {
        List<TransactionEntity> transactionsOfLastHour =
                repository.getTransactionsOfCardFromOtherIpInTimeInterval(
                        transaction.getNumber(),
                        transaction.getIp(),
                        transaction.getDate().minusHours(CHECK_HOURS_BEFORE_TRANSACTION),
                        transaction.getDate()
                );
        long uniqueByIpCount = countDistinctByField(transactionsOfLastHour, TransactionEntity::getIp);
        return getStatusBasedOnNumberOfTransactions(uniqueByIpCount);
    }

    private TransactionStatus getStatusBasedOnNumberOfTransactions(long numberOfCases) {
        TransactionStatus status = TransactionStatus.ALLOWED;
        if (numberOfCases > 2) {
            status = TransactionStatus.PROHIBITED;
        } else if (numberOfCases == 2) {
            status = TransactionStatus.MANUAL_PROCESSING;
        }
        return status;
    }

    public <T> long countDistinctByField(List<T> list, Function<T, Object> getFiledFunction) {
        return list.stream()
                .filter(new FieldDistincter<>(getFiledFunction))
                .count();
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