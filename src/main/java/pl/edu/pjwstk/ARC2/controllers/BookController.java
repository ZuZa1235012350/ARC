package pl.edu.pjwstk.ARC2.controllers;

import com.google.cloud.datastore.Key;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.ARC2.entities.Book;
import pl.edu.pjwstk.ARC2.service.BookService;
import pl.edu.pjwstk.ARC2.zad5.StreamObjectDownload;

import java.io.IOException;
import java.util.List;



@RestController
@AllArgsConstructor
public class BookController {

    private final BookService service;

    private final StreamObjectDownload streamObjectDownload;
//    @PostMapping("/setBookData/{title}/{author}/{counter}")
    @GetMapping("/setBookData/{title}/{author}/{counter}/{sectionName}")
    public Key setBookData(@PathVariable(value = "title")  String title, @PathVariable(value = "author") String author, @PathVariable(value = "counter") Long counter,@PathVariable(value = "sectionName") String sectionName) {
        return service.setBookData(title,author,counter,sectionName);
    }
    @GetMapping ("/listBooks")
    public List<Book> listBooks() {
        return service.getBooks();
    }

    @GetMapping("/getBookByTitle/{title}")
    public Book getBookByTitle(@PathVariable("title") String title){
        return service.getBookByTitle(title);
    }

    @GetMapping("/getBooksBySection/{sectionName}")
    public List<Book> getBooksBySection(@PathVariable("sectionName") String sectionName) throws Exception {
        return service.getBooksWithTheSameSection(sectionName);
    }

    @GetMapping("/rentBook/{title}")
    public String rentBook(@PathVariable("title") String title){
       return service.rentBook(title);
    }

    @GetMapping("/remind")
    public String remind(){
        return service.sendReminder();
    }

    @GetMapping("/setDataFromCsv")
    public String setDataFromCsvGCS() throws IOException {
        return streamObjectDownload.download();
    }



}
