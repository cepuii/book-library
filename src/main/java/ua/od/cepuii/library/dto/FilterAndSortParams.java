package ua.od.cepuii.library.dto;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@ToString
public class FilterAndSortParams implements Serializable {

    private String firstParam;
    private String secondParam;
    private String orderBy;
    private boolean descending;

    private long userId;

    public FilterAndSortParams(String firstValue, String secondValue, String orderBy, boolean descending) {
        this.firstParam = firstValue == null ? "" : firstValue;
        this.secondParam = secondValue == null ? "" : secondValue;
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
        return firstParam;
    }

    public String getSecondParam() {
        return secondParam;
    }

    public void setFirstParam(String firstParam) {
        this.firstParam = firstParam == null ? "" : firstParam;
    }

    public void setSecondParam(String secondParam) {
        this.secondParam = secondParam == null ? "" : secondParam;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
