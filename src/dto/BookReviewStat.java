package dto;

public class BookReviewStat {
    private int bookId;
    private double avgRating;
    private int reviewCount;

    public BookReviewStat(int bookId, double avgRating, int reviewCount) {
        this.bookId = bookId;
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
}
