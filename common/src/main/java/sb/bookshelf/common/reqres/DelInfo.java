package sb.bookshelf.common.reqres;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class DelInfo implements Serializable {

    private Long deleted = 0L;
    private List<String> ids = new ArrayList<>();

    public void append(String id) {
        ids.add(id);
        ++deleted;
    }

}
