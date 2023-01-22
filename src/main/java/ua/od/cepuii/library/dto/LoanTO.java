package ua.od.cepuii.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private String bookInfo;
}
