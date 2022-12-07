package pl.edu.pjwstk.ARC2.zad6;

import com.google.cloud.storage.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GCSUpload {

    public void uploadFile(String projectId, String bucketName, String objectName, byte[] fileContents, String contentType) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).setContentType(contentType).build();
//        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of("arc2-366516.appspot.com", objectName)).setContentType(contentType).build();
        try {
            storage.createFrom(blobInfo,new ByteArrayInputStream(fileContents));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
