package dto;

public class PopularBook {
    private int bookId;
    private double avgRating;
    private int rank;

    public PopularBook(int bookId, double avgRating, int rank) {
        this.bookId = bookId;
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
}
