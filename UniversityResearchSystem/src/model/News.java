package model;
import java.util.ArrayList;
import enums.NewsCategory;
import java.util.Date;
import java.util.List;

public class News {
    private int newsId;
    private String title;
    private String content;
    private String topic;
    private User author;
    private Date publishedDate;
    private boolean pinned;
    private List<Comment> comments;
    private NewsCategory category;
 
    public News(int newsId, String title, String content, String topic, User author) {
        this.newsId = newsId;
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.author = author;
        this.publishedDate = new Date();
        this.pinned = false;
        this.comments = new ArrayList<>();
    }
 
    public int getNewsId()         { return newsId; }
    public String getTitle()       { return title; }
    public String getContent()     { return content; }
    public String getTopic()       { return topic; }
    public User getAuthor()        { return author; }
    public Date getPublishedDate() { return publishedDate; }
    public boolean isPinned()      { return pinned; }
    public List<Comment> getComments() { return comments; }
 
    public void setTitle(String title)     { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setTopic(String topic)     { this.topic = topic; }
 
    public void addComment(Comment comment, User user) {
        comments.add(comment);
        System.out.println("[News] " + user.getFirstName() + " commented on '" + title + "'");
    }
 
    public void pin() {
        this.pinned = true;
        System.out.println("[News] '" + title + "' has been pinned.");
    }
 
    public void viewNews() {
        System.out.println("=== News: " + title + " ===");
        System.out.println("  Topic     : " + topic);
        System.out.println("  Author    : " + author.getFirstName() + " " + author.getLastName());
        System.out.println("  Published : " + publishedDate);
        System.out.println("  Pinned    : " + pinned);
        System.out.println("  Content   : " + content);
        System.out.println("  Comments  : " + comments.size());
    }
 
    @Override
    public String toString() {
        return "News{id=" + newsId + ", title='" + title + "', pinned=" + pinned + "}";
    }

	public NewsCategory getCategory() {
		return category;
	}

	public void setCategory(NewsCategory category) {
		this.category = category;
	}

}