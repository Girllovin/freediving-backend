package smallITgroup.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smallITgroup.payment.dto.PaypalPaymentDto;
import smallITgroup.payment.entity.Payment;
import smallITgroup.payment.dao.PaymentRepository;
import smallITgroup.accounting.dao.UserAccountRepository;
import smallITgroup.accounting.model.UserAccount;
import smallITgroup.accounting.dto.exeptions.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserAccountRepository userAccountRepository;

    public void processPayment(PaypalPaymentDto dto, String userEmail) {
        
        UserAccount user = userAccountRepository.findById(userEmail.trim())
                .orElseThrow(UserNotFoundException::new);

        Payment payment = new Payment(
                dto.getOrderId(),
                dto.getPayerId(),
                dto.getAmount(),
                dto.getCurrency(),
                userEmail,             
                dto.getPayerEmail(),    
                user,
                "COMPLETED"
        );

        paymentRepository.save(payment);
    }


    public boolean hasCompletedPayment(String email) {
        return paymentRepository.existsByUser_EmailAndStatus(email, "COMPLETED");
    }
}
