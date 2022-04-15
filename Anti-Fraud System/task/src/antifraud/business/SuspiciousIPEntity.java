package antifraud.business;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "suspicious_ip")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class SuspiciousIPEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String ip;
}
