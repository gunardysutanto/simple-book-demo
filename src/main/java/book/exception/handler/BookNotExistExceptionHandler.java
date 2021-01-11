package book.exception.handler;

import book.exception.BookNotExistException;
import book.exception.reporter.ErrorReporter;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BookNotExistExceptionHandler implements ExceptionMapper<BookNotExistException> {
    @Override
    public Response toResponse(BookNotExistException bookNotExistException) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorReporter(getErrorCodeFromApplicationConfiguration(),bookNotExistException.getMessage())).build();
    }

    private String getErrorCodeFromApplicationConfiguration() {
        return ConfigProvider.getConfig().getValue("error.code.book-not-found", String.class);
    }
}
