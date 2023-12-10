package sb.bookshelf.common.messages;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class TotalBooks implements Serializable {

    private Long count;

}
