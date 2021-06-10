package sb.bookshelf.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sb.bookshelf.common.model.Account;
import sb.bookshelf.web.dao.AccountDao;

@Service
@RequiredArgsConstructor
public final class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Account add(String username, String password) {
        return add(
                new Account(username, passwordEncoder.encode(password))
        );
    }

    @Override
    public Account add(Account account) {
        if (!exists(account))
            return accountDao.save(account);
        return null;
    }

    private boolean exists(String username) {
        return accountDao.findByUsername(username) != null;
    }

    @Override
    public boolean exists(Account account) {
        return exists(account.getUsername());
    }

    @Override
    public Long count() {
        return accountDao.count();
    }
}
