package book.exception;

public class DuplicateBookEntryException extends Exception {
    public DuplicateBookEntryException(String isbn) {
        super("Unable to process the new entry for this book since some book used that ISBN [ISBN = '+isbn+']. ");
    }
}
