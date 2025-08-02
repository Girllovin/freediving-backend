package smallITgroup.payment.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import smallITgroup.payment.entity.Payment;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
	
	boolean existsByUser_EmailAndStatus(String email, String status);

}
