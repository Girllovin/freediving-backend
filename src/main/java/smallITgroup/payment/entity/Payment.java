package smallITgroup.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.mongodb.core.mapping.DBRef;
import smallITgroup.accounting.model.UserAccount;

@Entity
@Table(name = "payments")
@Getter
@Setter	

public class Payment {

    @Id
    private String id;

    private String orderId;
    private String payerId;
    private BigDecimal amount;
    private String currency;
    private String userEmail;     // Email текущего пользователя (наш пользователь)
    private String payerEmail;    // Email плательщика из PayPal

    @DBRef
    private UserAccount user;

    private Instant timestamp;
    private String status;

    // Constructors
    public Payment() {}

    public Payment(String orderId, String payerId, BigDecimal amount, String currency, String userEmail, String payerEmail, UserAccount user, String status) {
        this.orderId = orderId;
        this.payerId = payerId;
        this.amount = amount;
        this.currency = currency;
        this.userEmail = userEmail;
        this.payerEmail = payerEmail;
        this.user = user;
        this.timestamp = Instant.now();
        this.status = status;
    }
}
