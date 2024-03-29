package ua.od.cepuii.library.dto;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@ToString
public class FilterParams implements Serializable {

    private String firstParam;
    private String secondParam;
    private String orderBy;
    private boolean descending;

    private long id;

    public FilterParams(String firstValue, String secondValue, String orderBy, boolean descending) {
        this.firstParam = firstValue == null ? "" : firstValue;
        this.secondParam = secondValue == null ? "" : secondValue;
        this.orderBy = orderBy == null ? "" : orderBy;
        this.descending = descending;
    }

    public static FilterParams cleanFilter() {
        return new FilterParams(null, null, null, false);
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

    public String getFirstParamForQuery() {
        return prepareForLike(validateForLike(firstParam));
    }

    public String getSecondParam() {
        return secondParam;
    }

    public String getSecondParamForQuery() {
        return prepareForLike(validateForLike(secondParam));
    }

    public void setFirstParam(String firstParam) {
        this.firstParam = firstParam == null ? "" : firstParam;
    }

    public void setSecondParam(String secondParam) {
        this.secondParam = secondParam == null ? "" : secondParam;
    }

    private String prepareForLike(String title) {
        return "%" + validateForLike(title) + "%";
    }

    private String validateForLike(String title) {
        return title.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "!]").replace("]", "!]").replace("^", "!^");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
