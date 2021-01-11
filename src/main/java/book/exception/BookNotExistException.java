package book.exception;

public class BookNotExistException extends Exception {
    public BookNotExistException(Long bookId) {
        super("Unable to find the book for this reference since it was not exist [Book ID = "+bookId+"].");
    }
}
