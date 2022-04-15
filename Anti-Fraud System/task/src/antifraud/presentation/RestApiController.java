package antifraud.presentation;

import antifraud.business.AmountDTO;
import antifraud.business.ResultDTO;
import antifraud.business.TransactionStatus;
import antifraud.exception.BadAmountException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class RestApiController {

    @PostMapping("/api/antifraud/transaction")
    public ResultDTO acceptTransaction(@RequestBody AmountDTO amountDTO) {
        return new ResultDTO(retrieveTransactionStatus(amountDTO.getAmount()).name());
    }

    public TransactionStatus retrieveTransactionStatus(long amount) {
        return Arrays.stream(TransactionStatus.values())
                .filter(val -> Math.max(val.getMin(), amount) == Math.min(amount, val.getMax()))
                .findFirst()
                .orElseThrow(BadAmountException::new);
    }
}
