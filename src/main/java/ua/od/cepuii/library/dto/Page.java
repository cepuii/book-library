package ua.od.cepuii.library.dto;

public class Page {

    private int currentPage;
    private int noOfRecords;

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setNoOfRecords(int noOfRecords) {
        this.noOfRecords = noOfRecords;
    }

    public void setPageAmount(int pageAmount) {
        this.pageAmount = pageAmount;
    }

    private int pageAmount;

    public Page(Builder b) {
        this.currentPage = b.currentPage;
        this.noOfRecords = b.noOfRecords;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getNoOfRecords() {
        return noOfRecords;
    }

    public int getPageAmount() {
        return pageAmount;
    }

    public static class Builder {

        private int pageAmount;
        private int noOfRecords;
        private int currentPage;

        public Builder pageAmount(int pageAmount) {
            this.pageAmount = pageAmount;
            return this;
        }

        public Builder noOfRecords(int noOfRecords) {
            this.noOfRecords = noOfRecords <= 0 ? 5 : noOfRecords;
            return this;
        }

        public Builder currentPage(int currentPage) {
            this.currentPage = currentPage <= 0 ? 1 : currentPage;
            return this;
        }

        public Page build() {
            return new Page(this);
        }
    }

}
