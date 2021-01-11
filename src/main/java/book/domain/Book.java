package book.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity(name="book")
@Data
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The book isbn should not be null or empty.")
    private String isbn;

    @NotBlank(message = "The book title should not be null or empty.")
    private String title;

    @NotBlank(message = "The book author should not be null or empty.")
    private String author;

    @Min(value= 1900, message = "The year publish should not less than 1900.")
    private int year;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Column(name = "last_modified_at")
    @Temporal(TemporalType.DATE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date lastModifiedAt;

    public Book mergeWith(Book modifiedBook) {
        if(getTitle().compareTo(modifiedBook.getTitle())>0)
            setTitle(modifiedBook.getTitle());
        if(getAuthor().compareTo(modifiedBook.getAuthor())>0)
            setAuthor(modifiedBook.getAuthor());
        if(getYear()!=modifiedBook.getYear())
            setYear(modifiedBook.getYear());
        return this;
    }

    @PrePersist
    public void beforeSaveNew() {
        setCreatedAt(new Date());
    }

    @PreUpdate
    public void beforeUpdate() {
        setLastModifiedAt(new Date());
    }
}
