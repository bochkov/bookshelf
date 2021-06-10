package sb.bookshelf.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public Account(String username, String password) {
        this(null, username, password);
    }

}
