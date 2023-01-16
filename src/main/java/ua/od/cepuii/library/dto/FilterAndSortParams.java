package ua.od.cepuii.library.dto;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@ToString
public class FilterAndSortParams implements Serializable {

    private final String[] filter = new String[4];
    private String orderBy;
    private boolean descending;

    public FilterAndSortParams(String firstParam, String firstValue, String secondParam, String secondValue, String orderBy, boolean descending) {
        this.filter[0] = firstParam;
        this.filter[1] = firstValue == null ? "" : firstValue;
        this.filter[2] = secondParam;
        this.filter[3] = secondValue == null ? "" : secondValue;
        this.orderBy = orderBy == null ? "" : orderBy;
        this.descending = descending;
    }

    public FilterAndSortParams(String firstValue, String secondValue, String orderBy, boolean descending) {
        this.filter[1] = firstValue == null ? "" : firstValue;
        this.filter[3] = secondValue == null ? "" : secondValue;
        this.orderBy = orderBy == null ? "" : orderBy;
        this.descending = descending;
    }

    public static FilterAndSortParams cleanFilter() {
        return new FilterAndSortParams(null, null, null, false);
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }

    public String getFirstParam() {
        return filter[1];
    }

    public String getSecondParam() {
        return filter[3];
    }

    public void setFirstParam(String firstParam) {
        filter[1] = firstParam == null ? "" : firstParam;
    }

    public void setSecondParam(String secondParam) {
        filter[3] = secondParam == null ? "" : secondParam;
    }
}
