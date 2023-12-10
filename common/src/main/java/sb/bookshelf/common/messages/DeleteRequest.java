package sb.bookshelf.common.messages;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class DeleteRequest {

    private List<String> ids = new ArrayList<>();

    public DeleteRequest(List<String> ids) {
        this.ids.addAll(ids);
    }

}
