package pl.edu.pjwstk.ARC2.zad5;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class StreamObjectDownload {
        public byte[] download() {
            Storage storage = StorageOptions.getDefaultInstance().getService();

        //    Blob blob = storage.get(BlobId.of(bucketName, objectName));
            Blob blob = storage.get(
                    BlobId.fromGsUtilUri("gs://arc2-366516.appspot.com/books.csv")
            );
            var content = blob.getContent();

            return content;

        }

}
