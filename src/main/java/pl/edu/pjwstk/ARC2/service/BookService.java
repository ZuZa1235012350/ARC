package pl.edu.pjwstk.ARC2.service;

import com.google.cloud.datastore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.ARC2.entities.Book;
import pl.edu.pjwstk.ARC2.repo.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class BookService implements BookRepository {
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("books");
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();




    @Override
    public Key setBookData(String title, String author, Long counter, String sectionName) {
        Key key = datastore.allocateId(keyFactory.newKey());
        Entity book = Entity.newBuilder(key)
                .set(
                        "title",
                        StringValue.newBuilder(title).build())
                .set(
                        "author",
                        StringValue.newBuilder(author).build())
                .set(
                        "sectionName",
                        StringValue.newBuilder(sectionName).build())
                .set(
                        "counter",
                        counter)
                .build();
        datastore.put(book);
        return key;
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
                book = tx.get(getBook(title).getKey());
                if (book.getLong("counter") > 0) {
                    book = Entity.newBuilder(book)
                            .set("counter", book.getLong("counter") - 1L)
                            .build();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            tx.update(book);
            tx.commit();
//            executorService.schedule(this::reminder, 5, TimeUnit.DAYS);
            //For demonstrations
            executorService.schedule(reminder, 2, TimeUnit.SECONDS);

            return String.format("counter is %s now is %s", Objects.requireNonNull(book).getLong("counter"),
                    Objects.requireNonNull(book).getLong("counter") - 1L);


        } catch (Exception e) {
            return "Doesn't work";
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
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

//    public void reminder() {
//        log.info("You should return the book by now");
//    }

    Runnable reminder = new Runnable() {
        public void run() {
            // Do something
            System.out.println("You should return the book by now");
        }
    };


}
