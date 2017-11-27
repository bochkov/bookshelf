package sb.bookshelf.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Account.COLLECTION_NAME)
public final class Account {

    public static final String COLLECTION_NAME = "accounts";

    @Id
    private String id;
    private String username;
    private String password;

    public static Account from(Account account, PasswordEncoder encoder) {
        return new Account(account.id, account.username, encoder.encode(account.password));
    }
}
