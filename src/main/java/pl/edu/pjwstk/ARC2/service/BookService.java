package pl.edu.pjwstk.ARC2.service;

import com.google.cloud.bigquery.*;
import com.google.cloud.datastore.*;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.ARC2.entities.Book;
import pl.edu.pjwstk.ARC2.repo.BookRepository;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class BookService implements BookRepository {
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("books");
    private final KeyFactory keyFactoryNotification = datastore.newKeyFactory().setKind("notification");

    @Override
   public Key setBookData(String title, String author, Long counter, String sectionName) {
        Key key = datastore.allocateId(keyFactory.newKey());
        Entity bookEntity = Entity.newBuilder(key)
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
        datastore.put(bookEntity);
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
        String returnedMessage;
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
//            executorService.schedule(this::reminder,
//                    2,
//                    TimeUnit.SECONDS);
            returnedMessage = String.format("counter is %s now is %s", Objects.requireNonNull(book).getLong("counter"),
                    Objects.requireNonNull(book).getLong("counter") - 1L);

        } catch (Exception e) {
            returnedMessage = "Doesn't work";
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        return returnedMessage;
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

    //Cloud Task
    @Override
    public String sendReminder() {
        Key key = datastore.allocateId(keyFactoryNotification.newKey());
        Entity notification = Entity.newBuilder(key)
                .set(
                        "message",
                        StringValue.newBuilder("You should return the book by now.").build())
                .build();
        datastore.put(notification);
        return "task scheduled";
    }
    @Override
    public void downloadDataFromGCS(){
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Blob blob = storage.get(
                BlobId.fromGsUtilUri("gs://arc2-366516.appspot.com/booksFiles/books.csv")
        );
       var decodedString =  new String(blob.getContent(), StandardCharsets.UTF_8);
       List<String> bookData = Arrays.asList(decodedString.split("\\r\\n"));
       for(int i=1; i<bookData.size();i++){
           var temp = bookData.get(i).split(",");
           this.setBookData(temp[0],temp[1], Long.valueOf(temp[2]),temp[3]);
       }
    }

    @Override
    public void addBookToBigQueryTable(String title, String author, Long counter, String sectionName) throws Exception {
        // Step 1: Initialize BigQuery service
        BigQuery bigquery = BigQueryOptions.newBuilder().setProjectId("arc2-366516")
                .build().getService();

        // Step 2: Prepare query job
        final String INSERT_BOOK =
               String.format("INSERT INTO `arc2-366516.sample_dataset.book` (title,author,counter,book_section) VALUES (%s,%s,%o,%s)"
                       ,title,author,counter,sectionName);

        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(INSERT_BOOK).build();


        // Step 3: Run the job on BigQuery
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).build());
        queryJob = queryJob.waitFor();

        if (queryJob == null) {
            throw new Exception("job no longer exists");
        }
        // once the job is done, check if any error occured
        if (queryJob.getStatus().getError() != null) {
            throw new Exception(queryJob.getStatus().getError().toString());
        }

        // Step 4: Display results
        // Here, we will print the total number of rows that were inserted
        JobStatistics.QueryStatistics stats = queryJob.getStatistics();
        Long rowsInserted = stats.getDmlStats().getInsertedRowCount();
        System.out.printf("%d rows inserted\n", rowsInserted);
    }

}
