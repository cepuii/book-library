package ua.od.cepuii.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The class represents a Page of the records from the database.
 * It is used to provide pagination functionality in the application.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Page implements Serializable {

    private int currentPage;
    private int noOfRecords;
    private int pageAmount;

    /**
     * Gets the starting index for the records.
     *
     * @return the starting index for the records
     */
    public int getOffset() {
        return getNoOfRecords() * (getCurrentPage() - 1);
    }

    /**
     * Gets the maximum number of records to retrieve from the database.
     *
     * @return the maximum number of records to retrieve from the database
     */
    public int getLimit() {
        return getNoOfRecords();
    }
}
