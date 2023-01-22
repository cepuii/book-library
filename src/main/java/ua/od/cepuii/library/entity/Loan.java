package ua.od.cepuii.library.entity;

import lombok.*;
import ua.od.cepuii.library.entity.enums.LoanStatus;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class Loan extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;
    private long userId;
    private long bookId;
    private LocalDate startDate;
    private int duration;
    private LoanStatus status;
    private String bookInfo;
    private int fine;

    @Builder
    public Loan(long id, long userId, long bookId, LocalDate startDate, int duration, LoanStatus status, int fine, String bookInfo) {
        super(id);
        this.userId = userId;
        this.bookId = bookId;
        this.startDate = startDate;
        this.duration = duration;
        this.status = status;
        this.fine = fine;
        this.bookInfo = bookInfo;
    }
}
