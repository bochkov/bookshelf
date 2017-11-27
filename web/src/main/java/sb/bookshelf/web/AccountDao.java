package sb.bookshelf.web;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountDao extends MongoRepository<Account, String> {

    Account findByUsername(String username);

}
