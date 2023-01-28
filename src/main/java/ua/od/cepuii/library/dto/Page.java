package ua.od.cepuii.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Page implements Serializable {

    private int currentPage;
    private int noOfRecords;
    private int pageAmount;

    public int getOffset() {
        return getNoOfRecords() * (getCurrentPage() - 1);
    }

    public int getLimit() {
        return getNoOfRecords();
    }
}
