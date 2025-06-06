package smallITgroup.accounting.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import smallITgroup.accounting.model.UserAccount;

// This interface extends MongoRepository to provide CRUD operations for the UserAccount entity
// MongoRepository is a Spring Data interface that simplifies interactions with MongoDB
public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    // In this case, UserAccountRepository will automatically have all the methods of MongoRepository,
    // such as save(), findAll(), findById(), delete(), etc., for the UserAccount entity.
    // The first generic parameter (UserAccount) is the type of the entity you're working with.
    // The second parameter (String) is the type of the entity's identifier (in this case, the primary key).

}
