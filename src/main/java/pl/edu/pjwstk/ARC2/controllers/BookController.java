package pl.edu.pjwstk.ARC2.controllers;

import com.google.cloud.datastore.Key;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.ARC2.entities.Book;
import pl.edu.pjwstk.ARC2.service.BookService;
import pl.edu.pjwstk.ARC2.zad4.CreateTask;

import java.util.List;



@RestController
@AllArgsConstructor
public class BookController {

    private final BookService service;
    private final CreateTask createTask;

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

    @GetMapping("/triggerImport")
    public void  triggerImport() throws Exception {
//       service.downloadDataFromGCS();
        createTask.addTaskForReadingFromGCS();
    }
    @GetMapping("/setDataFromCsv")
    public void  setDataFromCsvGCS() throws Exception {
        service.downloadDataFromGCS();
//        createTask.addTaskForReadingFromGCS();
    }



}
