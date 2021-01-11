package book.resource;

import book.domain.Book;
import book.domain.BookDataAccess;
import book.exception.BookNotExistException;
import book.exception.DuplicateBookEntryException;
import book.helper.PageRequest;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/books")
public class BookResource {
    @Inject
    private BookDataAccess bookDataAccess;

    @ConfigProperty(name = "pagination.maximum-data-per-page",defaultValue = "10")
    private int maximumDataInThePage;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllBooks(@QueryParam("pageNum") @DefaultValue("1") int pageNumber) {
        return Response.status(Response.Status.FOUND).entity(bookDataAccess.allBooks(PageRequest.of(pageNumber,maximumDataInThePage))).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookId}")
    public Response viewDetails(@PathParam("bookId") Long bookId) throws BookNotExistException {
        return Response.status(Response.Status.FOUND).entity(bookDataAccess.viewExisting(bookId)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response entryNewBook(@Valid Book book) throws DuplicateBookEntryException {
        return Response.status(Response.Status.CREATED).entity(bookDataAccess.newEntry(book)).build();
    }

    @PUT
    @Path("/{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("bookId") Long bookId, @Valid Book modifiedBook) throws BookNotExistException {
        modifiedBook = bookDataAccess.updateExisting(bookId,modifiedBook);
        return Response.ok(modifiedBook).build();
    }

    @DELETE
    @Path("/{bookId}")
    public Response deleteBook(@PathParam("bookId") Long bookId) throws BookNotExistException {
        bookDataAccess.removeExisting(bookId);
        return Response.noContent().build();
    }

}
