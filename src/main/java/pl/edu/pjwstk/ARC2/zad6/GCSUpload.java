package pl.edu.pjwstk.ARC2.zad6;

import com.google.cloud.storage.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GCSUpload {

    public void uploadFile(String objectName, byte[] file, String contentType) throws StorageException, IOException {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of("arc2-366516.appspot.com", objectName)).setContentType(contentType).build();
        storage.createFrom(blobInfo,new ByteArrayInputStream(file));
    }
}
