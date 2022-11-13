package pl.edu.pjwstk.ARC2.repo;

import com.google.cloud.datastore.Key;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.ARC2.entities.Book;

import java.util.List;

@Repository
public interface BookRepository {
    Key setBookData(String title,String author,Long counter, String sectionName);
    Book getBookByTitle(String title);
    List<Book> getBooks();
    List<Book> getBooksWithTheSameSection(String sectionName);
    void rentBook(String title);
}
