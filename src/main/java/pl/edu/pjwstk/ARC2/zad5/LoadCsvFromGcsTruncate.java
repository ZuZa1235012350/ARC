package pl.edu.pjwstk.ARC2.zad5;


import pl.edu.pjwstk.ARC2.service.BookService;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class LoadCsvFromGcsTruncate {
    private static BookService bookService;

    public List<String> runLoadCsvFromGcs() throws Exception {


        String sourceUri = "gs://arc2-366516.appspot.com/books.csv";
        Path path = Paths.get(URI.create(sourceUri));
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        return lines;
//        loadCsvFromGcs(lines);
    }
//    public void loadCsvFromGcs(List<String> lines) {
//      try{
//          lines.stream().map(line -> line.split(",")).forEach(temp -> bookService.setBookData(temp[0], temp[1], Long.valueOf(temp[2]), temp[3]));
//      }catch (Exception e){
//          e.printStackTrace();
//      }
//
//    }


}
