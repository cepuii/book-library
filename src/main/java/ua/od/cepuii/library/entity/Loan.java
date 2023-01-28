package ua.od.cepuii.library.entity;

import ua.od.cepuii.library.entity.enums.LoanStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Loan extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;

    private long authorId;
    private long bookId;
    private LocalDate startDate;
    private int duration;
    private LoanStatus status;
    private int fine;

    public Loan() {
    }

    public Loan(long id, long userId, long bookId, LocalDate startDate, int duration, LoanStatus status, int fine) {
        super(id);
        this.authorId = userId;
        this.bookId = bookId;
        this.startDate = startDate;
        this.duration = duration;
        this.status = status;
        this.fine = fine;
    }

    public Loan(long userId, long bookId, int duration) {
        this(0, userId, bookId, LocalDate.now(), duration, LoanStatus.RAW, 0);
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Loan loan = (Loan) o;

        if (authorId != loan.authorId) return false;
        if (duration != loan.duration) return false;
        if (fine != loan.fine) return false;
        if (bookId != loan.bookId) return false;
        if (!Objects.equals(startDate, loan.startDate)) return false;
        return status == loan.status;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (authorId ^ (authorId >>> 32));
        result = 31 * result + (int) (authorId ^ (authorId >>> 32));
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + duration;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + fine;
        return result;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", author_id=" + authorId +
                ", book_id=" + bookId +
                ", startDate=" + startDate +
                ", duration=" + duration +
                ", status=" + status +
                ", fine=" + fine +
                '}';
    }
}
