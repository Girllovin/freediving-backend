package smallITgroup.payment.controller;

import smallITgroup.payment.dto.PaypalPaymentDto;
import smallITgroup.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/success")
    public ResponseEntity<?> handlePaymentSuccess(@RequestBody PaypalPaymentDto paymentDto) {
        paymentService.processPayment(paymentDto, paymentDto.getUserEmail());
        return ResponseEntity.ok().build();
    }


}
