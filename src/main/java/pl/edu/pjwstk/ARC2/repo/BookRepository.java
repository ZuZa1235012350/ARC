package pl.edu.pjwstk.ARC2.repo;

import com.google.cloud.datastore.Key;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.ARC2.entities.Book;

import java.util.List;

@Repository
public interface BookRepository {
    public Key setBookData(String title,String author,Long counter, String sectionName);
    public String getBookSection(String title);
    public Book getBookByTitle(String title);
    public List<Book> getBooks();
}
