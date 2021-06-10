package sb.bookshelf.web.service;

import sb.bookshelf.common.model.Account;

public interface AccountService {

    Account add(String username, String password);

    Account add(Account account);

    boolean exists(Account account);

    Long count();
}
