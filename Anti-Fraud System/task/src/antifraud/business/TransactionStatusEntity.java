package antifraud.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@Table(name = "transaction_status")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionStatusEntity {
    @Id
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private long maxAmount;
}
