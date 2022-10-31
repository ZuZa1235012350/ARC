package pl.edu.pjwstk.ARC2.controllers;

import com.google.cloud.datastore.Key;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.ARC2.entities.Book;
import pl.edu.pjwstk.ARC2.service.BookService;

import java.util.List;


@RestController
@AllArgsConstructor
public class BookController {

    private final BookService service;
//    @PostMapping("/setBookData/{title}/{author}/{counter}")
    @GetMapping("/setBookData/{title}/{author}/{counter}/{sectionName}")
    public Key setBookData(@PathVariable(value = "title")  String title, @PathVariable(value = "author") String author, @PathVariable(value = "counter") Long counter,@PathVariable(value = "sectionName") String sectionName) {
        return service.setBookData(title,author,counter,sectionName);
    }

    @GetMapping ("/listBook")
    public List<Book> listBooks() {
        return service.getBooks();
    }

    @GetMapping("/getSectionName/{title}")
    public String getBookSectionName(@PathVariable("title") String title){
        return service.getBookSection(title);
    }

    @GetMapping("/getBookByTitle/{title}")
    public Book getBookByTitle(@PathVariable("title") String title){
        return service.getBookByTitle(title);
    }



}
