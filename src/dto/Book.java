package dto;

public class Book {
    private int bookId;
    private String title;
    private String genreName;
    private String authorName;

    public Book(int bookId, String title, String genreName, String authorName) {
        this.bookId = bookId;
        this.title = title;
        this.genreName = genreName;
        this.authorName = authorName;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getGenreName() {
        return genreName;
    }

    public String getAuthorName() {
        return authorName;
    }
} 
