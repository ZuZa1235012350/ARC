package pl.edu.pjwstk.ARC2.zad5;


import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class StreamObjectDownload {

    public String download() throws IOException {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId blobId = BlobId.of("arc2-366516.appspot.com", "books.csv");
        byte[] content = storage.readAllBytes(blobId);
        String contentString = new String(content, UTF_8);
        return contentString;
    }
}
