package pl.edu.pjwstk.ARC2;

import com.google.cloud.datastore.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.ARC2.request.BookRequest;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class BookController {

    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("book");

    @GetMapping("/setBookData/{title}/{author}/{counter}")
    public Key setUserData(@PathVariable(value = "title")  String title, @PathVariable(value = "author") String author, @PathVariable(value = "counter") int counter) {
        Key key = datastore.allocateId(keyFactory.newKey());
        Entity book = Entity.newBuilder(key)
                .set(
                        "title",
                        StringValue.newBuilder(title).setExcludeFromIndexes(true).build())
                .set(
                        "author",
                        StringValue.newBuilder(author).setExcludeFromIndexes(true).build())
                .set(
                        "counter",
                        counter)
                .build();
        datastore.put(book);
        return key;
    }

    @GetMapping ("/listBook")
    public List<BookRequest> listCustomer() {
        List<BookRequest> listOfEntities = new ArrayList<>();
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("book")
                .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity currentEntity = results.next();
            listOfEntities.add(new BookRequest(currentEntity.getString("title"),currentEntity.getString("author"),Integer.parseInt(currentEntity.getString("counter"))));
        }
        return listOfEntities;
    }


}
