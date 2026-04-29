package model;
import enums.PaperType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
public class ResearchPaper {
    private int paperId;
    private String title;
    private int citations;
    private int pages;
    private String topic;
    private String doc;
    private Date datePublished;
    private List<String> authorNames;
    private PaperType type;
    
    public ResearchPaper(int paperId, String title, String topic, int pages, String doc) {
        this.paperId = paperId;
        this.title = title;
        this.topic = topic;
        this.pages = pages;
        this.doc = doc;
        this.citations = 0;
        this.datePublished = new Date();
        this.authorNames = new ArrayList<>();
    }
 
    public int getPaperId()            { return paperId; }
    public String getTitle()           { return title; }
    public int getCitations()          { return citations; }
    public int getPages()              { return pages; }
    public String getTopic()           { return topic; }
    public String getDoc()             { return doc; }
    public Date getDatePublished()     { return datePublished; }
    public List<String> getAuthorNames() { return authorNames; }
 
    public void setTitle(String title) { this.title = title; }
    public void setTopic(String topic) { this.topic = topic; }
    public void setDoc(String doc)     { this.doc = doc; }
 
    public void addAuthorName(String name) {
        authorNames.add(name);
    }
 
    @SuppressWarnings("deprecation")
	public String getCitation(String format) {
        String last  = authorNames.isEmpty() ? "Unknown" : authorNames.get(0).split(" ")[1];
        String first = authorNames.isEmpty() ? ""        : authorNames.get(0).split(" ")[0];
        int year = 1900 + datePublished.getYear();
        String citation;
        if (format.equalsIgnoreCase("APA")) {
            citation = last + " (" + year + "). " + title + ". " + topic + ".";
        } else if (format.equalsIgnoreCase("MLA")) {
            citation = last + ", " + first + ". \"" + title + ".\" " + topic + ", " + year + ".";
        } else {
            citation = title + " by " + first + " " + last;
        }
        System.out.println("[Citation - " + format + "] " + citation);
        return citation;
    }
 
    public void addCite() {
        citations++;
        System.out.println("[ResearchPaper] '" + title + "' cited. Total: " + citations);
    }
 
    @Override
    public String toString() {
        return "ResearchPaper{id=" + paperId + ", title='" + title
                + "', citations=" + citations + ", pages=" + pages + "}";
    }

	public PaperType getType() {
		return type;
	}

	public void setType(PaperType type) {
		this.type = type;
	}
}
