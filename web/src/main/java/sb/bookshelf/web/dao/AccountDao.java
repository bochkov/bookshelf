package sb.bookshelf.web.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import sb.bookshelf.common.model.Account;

public interface AccountDao extends MongoRepository<Account, String> {

    Account findByUsername(String username);

}
