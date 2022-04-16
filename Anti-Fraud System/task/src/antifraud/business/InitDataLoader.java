package antifraud.business;

import antifraud.persistence.TransactionStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean isDBInitialized = false;
    private final List<TransactionStatusEntity> statuses = List.of(
            new TransactionStatusEntity(TransactionStatus.ALLOWED,200),
            new TransactionStatusEntity(TransactionStatus.MANUAL_PROCESSING, 1500),
            new TransactionStatusEntity(TransactionStatus.PROHIBITED, Long.MAX_VALUE)
    );

    @Autowired
    private TransactionStatusRepository statusRepo;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!isDBInitialized) {
            statuses.forEach(this::createStatus);
            isDBInitialized = true;
        }
    }

    private void createStatus(TransactionStatusEntity status) {
        boolean isStatusExists = statusRepo.existsById(status.getStatus());
        if (!isStatusExists) {
            statusRepo.save(status);
        }
    }
}
