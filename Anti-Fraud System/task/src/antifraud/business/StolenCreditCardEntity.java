package antifraud.business;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "stolen_credit_card")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class StolenCreditCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String number;
}
