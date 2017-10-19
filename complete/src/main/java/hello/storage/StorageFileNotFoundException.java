// File originally part of "Getting Started - Uploading Files"-Tutorial (https://spring.io/guides/gs/uploading-files/)

package hello.storage;

public class StorageFileNotFoundException extends StorageException {

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}