package ua.od.cepuii.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * The LoanTO class is a Transfer Object (DTO) class that represents a loan in the library system.
 * It includes the book's id, user's id, loan start date, loan end date, loan status, fine amount, user's email and
 * book's info.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanTO {
    private static final long serialVersionUID = 1;
    private long id;
    private long userId;
    private long bookId;
    private Date startDate;
    private Date endDate;
    private String status;
    private int fine;
    private String userEmail;
    private String bookInfo;
}
