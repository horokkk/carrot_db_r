package dto;

public class BookReviewStat {
    private int bookId;
    private String bookTitle;
    private double avgRating;
    private int reviewCount;

    public BookReviewStat(int bookId, String bookTitle, double avgRating, int reviewCount) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.avgRating = avgRating;
        this.reviewCount = reviewCount;
    }

    public int getBookId() {
        return bookId;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
