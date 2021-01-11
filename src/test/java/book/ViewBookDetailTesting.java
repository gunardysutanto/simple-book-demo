package book;

import book.domain.Book;
import book.domain.BookDataAccess;
import book.exception.BookNotExistException;
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
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ViewBookDetailTesting {
    @Inject
    private BookDataAccess bookDataAccess;

    @ConfigProperty(name = "error.code.book-not-found")
    private String errorCodeForBookNotExist;

    private Book sampleBook;

    @BeforeAll
    public void dataSetup() throws DuplicateBookEntryException {
        sampleBook = new Book();
        sampleBook.setIsbn("978-1444-1539-10");
        sampleBook.setTitle("Living German");
        sampleBook.setAuthor("R.W. Buckly");
        sampleBook.setYear(2015);
        sampleBook = bookDataAccess.newEntry(sampleBook);
    }

    @Test
    public void viewBookDetailWithExistingBook() {
        given().contentType(MediaType.APPLICATION_JSON).pathParams("bookId",sampleBook.getId()).get("/books/{bookId}").then().statusCode(Response.Status.FOUND.getStatusCode())
                .body("isbn",is(sampleBook.getIsbn()))
                .body("title",is(sampleBook.getTitle()))
                .body("author",is(sampleBook.getAuthor()))
                .body("year",is(sampleBook.getYear()));
    }


    @Test
    public void viewBookDetailWithNonExistingBook() {
        var nonExistBookId = 123L;

        given().contentType(MediaType.APPLICATION_JSON).pathParams("bookId",nonExistBookId).get("/books/{bookId}").then().statusCode(Response.Status.BAD_REQUEST.getStatusCode()).body("errorCode",is(errorCodeForBookNotExist));
    }

    @AfterAll
    public void dataCleanUp() {
        bookDataAccess.removeAllBooks();
    }
}
