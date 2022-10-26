package pl.edu.pjwstk.ARC2.entities;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Section {
    private String name;
//    ImmutableSet<Book> books;

    public Section(String name) {
        this.name=name;
    }
}
