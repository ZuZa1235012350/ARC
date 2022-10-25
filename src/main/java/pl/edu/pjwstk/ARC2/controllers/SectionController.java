package pl.edu.pjwstk.ARC2.controllers;


import com.google.cloud.datastore.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.ARC2.request.SectionRequest;

import java.util.ArrayList;
import java.util.List;


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

    @GetMapping ("/listSection")
    public List<SectionRequest> listBooks() {
        List<SectionRequest> listOfEntities = new ArrayList<>();
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("book")
                .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity currentEntity = results.next();
            listOfEntities.add(new SectionRequest(currentEntity.getString("name")));
        }
        return listOfEntities;
    }
}
