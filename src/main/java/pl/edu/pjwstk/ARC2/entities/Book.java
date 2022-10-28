package pl.edu.pjwstk.ARC2.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {
    private String title;
    private String author;
    private Integer counter;
    private String genre;

    public Book(String title, String author, Integer counter, String genre) {
        this.title = title;
        this.author = author;
        this.counter = counter;
        this.genre = genre;
    }
    @Override
    public String toString() {
        return "Book{" +
                ", title='" + this.title + '\'' +
                ", author='" + this.author + '\'' +
                ", counter='" + this.counter + '\'' +
                ", genre=" + this.genre +
                '}';
    }
}
