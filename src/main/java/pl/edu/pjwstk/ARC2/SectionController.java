package pl.edu.pjwstk.ARC2;


import com.google.cloud.datastore.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SectionController {
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("section");

//    @PostMapping("/setSectionName/{name}")
    @GetMapping("/setSectionName/{name}")
    public Key setSectionData(@PathVariable(value = "name")  String name) {
        Key key = datastore.allocateId(keyFactory.newKey());
        Entity section = Entity.newBuilder(key)
                .set(
                        "name",
                        StringValue.newBuilder(name).setExcludeFromIndexes(true).build())
                .build();
        datastore.put(section);
        return key;
    }
}
