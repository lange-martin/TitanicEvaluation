// File originally part of "Getting Started - Uploading Files"-Tutorial (https://spring.io/guides/gs/uploading-files/)

package hello.storage;

public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
