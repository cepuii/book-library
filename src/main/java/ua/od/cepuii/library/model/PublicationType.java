package ua.od.cepuii.library.model;

public enum PublicationType {
    BOOK("Book"), JOURNAL("Journal"), ARTICLE("Article"), NEWSPAPER("Newspaper");

    private final String name;

    PublicationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
