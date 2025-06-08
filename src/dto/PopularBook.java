package dto;

public class PopularBook {
    private int bookId;
    private double avgRating;
    private String bookTitle;
    private int rank;

    public PopularBook(int bookId, String bookTitle, double avgRating, int rank) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.avgRating = avgRating;
        this.rank = rank;
    }

    public int getBookId() {
        return bookId;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public int getRank() {
        return rank;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
