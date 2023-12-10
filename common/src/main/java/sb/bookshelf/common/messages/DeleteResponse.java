package sb.bookshelf.common.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class DeleteResponse implements Serializable {

    private int deleted;
    private List<String> ids = new ArrayList<>();

    public DeleteResponse(List<String> ids) {
        this.ids = ids;
        this.deleted = ids.size();
    }

}
