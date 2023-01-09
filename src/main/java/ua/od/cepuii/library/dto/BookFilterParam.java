package ua.od.cepuii.library.dto;

public class BookFilterParam {

    private String title;

    private String author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookFilterParam(String title, String author) {
        this.title = title == null ? "" : title;
        this.author = author == null ? "" : author;
    }
}
