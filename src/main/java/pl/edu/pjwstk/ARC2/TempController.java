package pl.edu.pjwstk.ARC2;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TempController {
    @GetMapping("/")
    public String helloPage() {
        return "Hello";
    }
}

