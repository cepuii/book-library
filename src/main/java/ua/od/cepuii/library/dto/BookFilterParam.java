package ua.od.cepuii.library.dto;

import java.io.Serializable;

public class BookFilterParam implements Serializable {

    private String title;

    private String author;

    private String orderBy;
    private boolean descending;

    public BookFilterParam() {
    }

    public BookFilterParam(String title, String author, String orderBy, boolean descending) {
        this.title = title == null ? "" : title;
        this.author = author == null ? "" : author;
        this.orderBy = orderBy == null ? "b_title" : orderBy;
        this.descending = descending;
    }

    public static BookFilterParam cleanFilter() {
        return new BookFilterParam(null, null, null, false);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? "" : author;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy == null ? "b_title" : orderBy;
    }

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", orderBy='" + orderBy + '\'' +
                ", descending=" + descending +
                '}';
    }
}
