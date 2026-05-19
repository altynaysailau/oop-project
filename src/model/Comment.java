package model;
import java.util.Date;

public class Comment {
    private int commentId;
    private User author;
    private String text;
    private Date date;
 
    public Comment(int commentId, User author, String text) {
        this.commentId = commentId;
        this.author = author;
        this.text = text;
        this.date = new Date();
    }
 
    public Comment(String text2, User user) {
		
	}

	public int getCommentId()  { return commentId; }
    public User getAuthor()    { return author; }
    public String getText()    { return text; }
    public Date getDate()      { return date; }
    public void setText(String text) { this.text = text; }
 
    public void pin() {
        System.out.println("[Comment #" + commentId + "] Pinned by " + author.getFirstName());
    }
 
    @Override
    public String toString() {
        return "Comment{id=" + commentId + ", author=" + author.getFirstName()
                + ", text='" + text + "'}";
    }
}