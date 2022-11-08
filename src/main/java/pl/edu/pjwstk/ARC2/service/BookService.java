package pl.edu.pjwstk.ARC2.service;

import com.google.cloud.datastore.*;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.ARC2.entities.Book;
import pl.edu.pjwstk.ARC2.repo.BookRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService implements BookRepository {
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("books");

    @Override
    public Key setBookData(String title, String author, Long counter, String sectionName) {
        Key key = datastore.allocateId(keyFactory.newKey());
        Entity book = Entity.newBuilder(key)
                .set(
                        "title",
                        StringValue.newBuilder(title).setExcludeFromIndexes(true).build())
                .set(
                        "author",
                        StringValue.newBuilder(author).setExcludeFromIndexes(true).build())
                .set(
                        "sectionName",
                        StringValue.newBuilder(sectionName).setExcludeFromIndexes(true).build())
                .set(
                        "counter",
                        counter)
                .build();
        datastore.put(book);
        return key;
    }

    @Override
    public String getBookSection(String title) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("books")
                .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()){
            Entity currentEntity = results.next();
            if (currentEntity.getString("username").equals(title)){
                return currentEntity.getString("sectionName");
            }
        }
        return null;
    }

    @Override
    public Book getBookByTitle(String title) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("books")
                .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()){
            Entity currentEntity = results.next();
            if (currentEntity.getString("title").equals(title)){
                return new Book(
                        currentEntity.getString("title"),
                        currentEntity.getString("author"),
                        currentEntity.getLong("counter"),
                        currentEntity.getString("sectionName")
                        );
            }
        }
        return null;
    }

    @Override
    public List<Book> getBooks() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("books")
                .build();
        List<Book> listOfEntities = new ArrayList<>();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity currentEntity = results.next();
            listOfEntities.add(new Book(
                    currentEntity.getString("title"),
                    currentEntity.getString("author"),
                    currentEntity.getLong("counter"),
                    currentEntity.getString("sectionName")));
        }
        return listOfEntities;
    }
}
