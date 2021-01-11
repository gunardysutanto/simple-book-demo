package book;

import book.domain.Book;
import book.domain.BookDataAccess;
import book.exception.DuplicateBookEntryException;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NewBookEntryTesting {
    @Inject
    private BookDataAccess bookDataAccess;

    @ConfigProperty(name = "error.code.book-duplicate-entry")
    private String errorCodeForDuplicateBookEntry;


    @Test
    public void newBookEntry() throws DuplicateBookEntryException {
        var book = new Book();
        book.setIsbn("978-1444-1539-10");
        book.setTitle("Living German");
        book.setAuthor("R.W. Buckly");
        book.setYear(2015);
        given().body(book).contentType(MediaType.APPLICATION_JSON).post("/books").then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("isbn",is(book.getIsbn()))
                .body("title",is(book.getTitle()))
                .body("author",is(book.getAuthor()))
                .body("year",is(book.getYear()));
    }

    @Test
    public void newBookEntryWithSameBook() throws DuplicateBookEntryException {
        var theSameBook = new Book();
        theSameBook.setIsbn("978-1444-1539-10");
        theSameBook.setTitle("Living German");
        theSameBook.setAuthor("R.W. Buckly");
        theSameBook.setYear(2015);
        given().body(theSameBook).contentType(MediaType.APPLICATION_JSON).post("/books").then()
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body("errorCode",is(errorCodeForDuplicateBookEntry));
    }

    @AfterAll
    public void dataCleanUp() {
        bookDataAccess.removeAllBooks();
    }
}
