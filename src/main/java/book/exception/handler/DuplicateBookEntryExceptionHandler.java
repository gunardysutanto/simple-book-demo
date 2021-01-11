package book.exception.handler;

import book.exception.DuplicateBookEntryException;
import book.exception.reporter.ErrorReporter;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DuplicateBookEntryExceptionHandler implements ExceptionMapper<DuplicateBookEntryException> {
    @Override
    public Response toResponse(DuplicateBookEntryException duplicateBookEntryException) {
        return Response.status(Response.Status.CONFLICT).entity(new ErrorReporter(getErrorCodeFromApplicationConfiguration(),duplicateBookEntryException.getMessage())).build();
    }

    private String getErrorCodeFromApplicationConfiguration() {
        return ConfigProvider.getConfig().getValue("error.code.book-duplicate-entry", String.class);
    }
}
