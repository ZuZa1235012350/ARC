package pl.edu.pjwstk.ARC2.service;

import com.google.cloud.datastore.*;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.ARC2.entities.Book;
import pl.edu.pjwstk.ARC2.repo.BookRepository;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class BookService implements BookRepository {
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("books");

    private static final Logger LOG = Logger.getLogger(BookService.class
            .getName());


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

//    @Override
//    public String getBookSection(String title) {
//        Query<Entity> query = Query.newEntityQueryBuilder()
//                .setKind("books")
//                .build();
//        QueryResults<Entity> results = datastore.run(query);
//        while (results.hasNext()){
//            Entity currentEntity = results.next();
//            if (currentEntity.getString("username").equals(title)){
//                return currentEntity.getString("sectionName");
//            }
//        }
//        return null;
//    }

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
    public List<Book> getBooksWithTheSameSection(String sectionName) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("books")
                .build();
        QueryResults<Entity> results = datastore.run(query);
        List<Book> listOfEntities = new ArrayList<>();
        while (results.hasNext()){
            Entity currentEntity = results.next();
            if (currentEntity.getString("sectionName").equals(sectionName)){
                listOfEntities.add(new Book(
                        currentEntity.getString("title"),
                        currentEntity.getString("author"),
                        currentEntity.getLong("counter"),
                        currentEntity.getString("sectionName")));
            }
        }
        return listOfEntities;
    }

    public Entity getBook(String title) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("books")
                .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()){
            Entity currentEntity = results.next();
            if (currentEntity.getString("title").equals(title)){
                return currentEntity;
            }
        }
        return null;
    }

    @Override
    public String rentBook(String title) {
        Transaction tx = datastore.newTransaction();
        Entity book = null;
        try {
            try {
//                book = getBook(title);
                book = tx.get(getBook(title).getKey());
                var count = book.getLong("count");
                if (count != 0) {
                    Entity.newBuilder(book)
                            .set("counter", count - 1)
                            .build();
                    return "Book was rented";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            datastore.put(book);
            tx.commit();
        } catch (ConcurrentModificationException e) {
            LOG.log(Level.WARNING,
                    "You may need more. Consider adding more.");
            LOG.log(Level.WARNING, e.toString(), e);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.toString(), e);
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        return "";
    }
//    public final void increment() {
//        int shardNum = generator.nextInt(NUM_SHARDS);
//        Key shardKey = KeyFactory.createKey("SimpleCounterShard",
//                Integer.toString(shardNum));
//
//        Transaction tx = datastore.beginTransaction();
//        Entity shard;
//        try {
//            try {
//                shard = datastore.get(tx, shardKey);
//                long count = (Long) shard.getProperty("count");
//                shard.setUnindexedProperty("count", count + 1L);
//            } catch (EntityNotFoundException e) {
//                shard = new Entity(shardKey);
//                shard.setUnindexedProperty("count", 1L);
//            }
//            DS.put(tx, shard);
//            tx.commit();
//        } catch (ConcurrentModificationException e) {
//            LOG.log(Level.WARNING,
//                    "You may need more shards. Consider adding more shards.");
//            LOG.log(Level.WARNING, e.toString(), e);
//        } catch (Exception e) {
//            LOG.log(Level.WARNING, e.toString(), e);
//        } finally {
//            if (tx.isActive()) {
//                tx.rollback();
//            }
//        }
//    }

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
