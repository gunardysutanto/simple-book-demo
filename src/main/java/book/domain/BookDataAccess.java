package book.domain;

import book.exception.BookNotExistException;
import book.exception.DuplicateBookEntryException;
import book.helper.PageRequest;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Slf4j
@SuppressWarnings("unchecked")
public class BookDataAccess {
    @Inject
    private EntityManager entityManager;

    public List<Book> allBooks(PageRequest pageRequest) {
        log.info("allBooks");
        return entityManager.createQuery("select b from book b").setFirstResult(pageRequest.getStartOffset()).setMaxResults(pageRequest.getMaximumDataPerPage()).getResultList();
    }

    public Book viewExisting(Long bookId) throws BookNotExistException {
        log.info("viewExisting[Book ID = "+bookId+"]");
        return findBookFor(bookId).orElseThrow(()-> new BookNotExistException(bookId));
    }

    @Transactional
    public Book newEntry(Book book) throws DuplicateBookEntryException {
        log.info("newEntry");
        if(anyBookUsedThisISBN(book.getIsbn()))
            throw new DuplicateBookEntryException(book.getIsbn());
        else {
            entityManager.persist(book);
            return (Book)entityManager.createQuery("select b from book b where b.isbn=:isbn").setParameter("isbn",book.getIsbn()).getSingleResult();
        }
    }

    @Transactional
    public Book updateExisting(Long bookId, Book modifiedBook) throws BookNotExistException {
        log.info("updateExisting[Book ID = "+bookId+"]");
        var mergedBook = findBookFor(bookId).map(existingBook->existingBook.mergeWith(modifiedBook)).orElseThrow(()-> new BookNotExistException(bookId));
        entityManager.persist(mergedBook);
        return mergedBook;
    }

    @Transactional
    public void removeExisting(Long bookId) throws BookNotExistException {
        log.info("removeExisting[Book ID= "+bookId+"]");
        var book = findBookFor(bookId).orElseThrow(()-> new BookNotExistException(bookId));
        entityManager.remove(book);
    }

    @Transactional
    public void removeAllBooks() {
        var query = entityManager.createQuery("delete from book");
        query.executeUpdate();
    }

    private Optional<Book> findBookFor(Long bookId) {
        return Optional.ofNullable(entityManager.find(Book.class,bookId));
    }

    private boolean anyBookUsedThisISBN(String isbn) {
        return (!entityManager.createQuery("select book from book book where book.isbn=:isbn").setParameter("isbn",isbn).getResultList().isEmpty());
    }

    public int count() {
        return entityManager.createQuery("select book from book book").getResultList().size();
    }
}
