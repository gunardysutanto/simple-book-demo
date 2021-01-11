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
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RemoveExistingBookTesting {
   @Inject
   private BookDataAccess bookDataAccess;

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
   public void removingWithExisting() {
       given().pathParams("bookId",sampleBook.getId()).delete("/books/{bookId}").then().statusCode(Response.Status.NO_CONTENT.getStatusCode());
   }

   @Test
   public void removingWithNonExistingBook() {
       var nonExistBookId = 123L;
       given().pathParams("bookId",nonExistBookId).delete("/books/{bookId}").then().statusCode(Response.Status.BAD_REQUEST.getStatusCode());
   }

   @AfterAll
   public void dataCleanUp() {
       bookDataAccess.removeAllBooks();
   }
}
