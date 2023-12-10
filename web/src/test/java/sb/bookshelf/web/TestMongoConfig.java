package sb.bookshelf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;

@Profile("test")
@TestConfiguration
public class TestMongoConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @EventListener(classes = {ContextClosedEvent.class})
    public void dbClean() {
        mongoTemplate.getDb().drop();
    }

}
