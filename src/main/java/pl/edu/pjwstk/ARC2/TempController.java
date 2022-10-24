package pl.edu.pjwstk.ARC2;

import com.google.cloud.datastore.*;
import com.sun.jdi.IntegerValue;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TempController {

    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    // Create a Key factory to construct keys associated with this project.
    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("book");

    @GetMapping("/")
    public String helloPage() {
        return "Hello";
    }
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
}

