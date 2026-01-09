package sb.bookshelf.common.messages;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public final class DeleteRequest {

    private List<String> ids = new ArrayList<>();

    public DeleteRequest(List<String> ids) {
        this.ids.addAll(ids);
    }

}
