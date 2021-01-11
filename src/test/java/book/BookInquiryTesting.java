package book;

import book.domain.Book;
import book.domain.BookDataAccess;
import book.exception.DuplicateBookEntryException;
import io.quarkus.test.junit.QuarkusTest;
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
public class BookInquiryTesting {
    @Inject
    private BookDataAccess bookDataAccess;

    @BeforeAll
    public void dataSetup() throws DuplicateBookEntryException {
        var book = new Book();
        book.setIsbn("978-1444-1539-10");
        book.setTitle("Living German");
        book.setAuthor("R.W. Buckly");
        book.setYear(2015);
        bookDataAccess.newEntry(book);
    }

    @Test
    public void bookInquiry() {
        var dataCount = bookDataAccess.count();
        given().contentType(MediaType.APPLICATION_JSON).get("/books").then().statusCode(Response.Status.FOUND.getStatusCode()).body("size()",is(dataCount));
    }

    @AfterAll
    public void dataCleanUp() {
        bookDataAccess.removeAllBooks();
    }
}
