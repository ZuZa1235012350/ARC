package pl.edu.pjwstk.ARC2.zad5;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.nio.file.Paths;

public class StreamObjectDownload {

//    public String download() throws IOException {
//
//        try {
//            Storage storage = StorageOptions.getDefaultInstance().getService();
//            BlobId blobId = BlobId.of("arc2-366516.appspot.com", "books.csv");
//            byte[] content = storage.readAllBytes(blobId);
//            String contentString = new String(content, UTF_8);
//            return contentString;
//        }catch (Exception e ){
//            return "Doesn't work :(";
//        }
//
//    }
public String download(String bucketName, String objectName, String destFilePath) {
    // The ID of your GCP project
    // String projectId = "your-project-id";

    // The ID of your GCS bucket
    // String bucketName = "your-unique-bucket-name";

    // The ID of your GCS object
    // String objectName = "your-object-name";

    // The path to which the file should be downloaded
    // String destFilePath = "/local/path/to/file.txt";

    Storage storage = StorageOptions.getDefaultInstance().getService();

    Blob blob = storage.get(BlobId.of(bucketName, objectName));
    blob.downloadTo(Paths.get(destFilePath));

    return
            "Downloaded object "
                    + objectName
                    + " from bucket name "
                    + bucketName
                    + " to "
                    + destFilePath;
}

}
