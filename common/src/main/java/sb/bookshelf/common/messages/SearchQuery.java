package sb.bookshelf.common.messages;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class SearchQuery implements Serializable {

    private String query;

}
