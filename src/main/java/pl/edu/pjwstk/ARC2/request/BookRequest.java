package pl.edu.pjwstk.ARC2.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {
    private String title, author;
    private int counter,id;

    public BookRequest(String title, String author, int counter) {
        this.title = title;
        this.author = author;
        this.counter = counter;
    }

    @Override
    public String toString() {
        return "BookRequest{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", counter='" + counter + '\'' +
                ", id=" + id +
                '}';
    }
}
