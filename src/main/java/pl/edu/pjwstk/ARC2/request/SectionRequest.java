package pl.edu.pjwstk.ARC2.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class SectionRequest {
    private String name;

    public SectionRequest(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SectionRequest{" +
                "name=" + name + '\'' +
                '}';
    }
}
