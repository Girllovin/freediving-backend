package smallITgroup.payment.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PaypalPaymentDto {
	
    private String orderId;
    private String payerId;
    private BigDecimal amount;
    private String currency;
    private String payerEmail;  
    private String userEmail;   
}
