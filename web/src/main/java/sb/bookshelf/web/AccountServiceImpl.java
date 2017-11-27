package sb.bookshelf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public final class AccountServiceImpl implements AccountSevice {

    private final AccountDao accountDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountDao accountDao, PasswordEncoder passwordEncoder) {
        this.accountDao = accountDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(Account account) {
        accountDao.save(Account.from(account, passwordEncoder));
    }

    public boolean isEmpty() {
        return accountDao.findAll().isEmpty();
    }
}
