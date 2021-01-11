package book;

import book.domain.Book;
import book.domain.BookDataAccess;
import book.exception.DuplicateBookEntryException;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateExistingBookTesting {
    @Inject
    private BookDataAccess bookDataAccess;

    @ConfigProperty(name = "error.code.book-not-found")
    private String errorCodeForBookNotExist;

    private Book existingBook;

    @BeforeAll
    public void dataSetup() throws DuplicateBookEntryException {
        existingBook = new Book();
        existingBook.setIsbn("978-1444-1539-10");
        existingBook.setTitle("Living German");
        existingBook.setAuthor("R.W. Buckly");
        existingBook.setYear(2015);
        existingBook = bookDataAccess.newEntry(existingBook);
    }

    @Test
    public void updateWithExistingBook() {
        existingBook.setYear(2019);
        given().contentType(MediaType.APPLICATION_JSON).body(existingBook).pathParams("bookId",existingBook.getId()).put("/books/{bookId}").then().statusCode(Response.Status.OK.getStatusCode())
                .body("isbn",is(existingBook.getIsbn()))
                .body("title",is(existingBook.getTitle()))
                .body("author",is(existingBook.getAuthor()))
                .body("year",is(existingBook.getYear()));
    }

    @Test
    public void updateWithNonExistingBook() {
        var nonExistingBookId= 123L;
        given().contentType(MediaType.APPLICATION_JSON).body(existingBook).pathParams("bookId",nonExistingBookId).put("/books/{bookId}").then().statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("errorCode",is(errorCodeForBookNotExist));

    }

    @AfterAll
    public void dataCleanup() {
        bookDataAccess.removeAllBooks();
    }
}
