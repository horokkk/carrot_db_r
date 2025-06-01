package dto;

public class Review {
    private int reviewId;
    private int bookId;
    private String userId;
    private String content;
    private int rating;
    private String date;

    public Review(int reviewId, int bookId, String userId, String content, int rating, String date) {
        this.reviewId = reviewId;
        this.bookId = bookId;
        this.userId = userId;
        this.content = content;
        this.rating = rating;
        this.date = date;
    }

    public int getReviewId() { return reviewId; }
    public int getBookId() { return bookId; }
    public String getUserId() { return userId; }
    public String getContent() { return content; }
    public int getRating() { return rating; }
    public String getDate() { return date; }
} 
