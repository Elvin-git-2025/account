package az.kapitalbank.mb.bff.transfermobile.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceResponse {
    private BigDecimal balance;
}
