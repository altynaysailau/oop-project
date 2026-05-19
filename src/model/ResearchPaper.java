package model;

import enums.CitationFormat;
import enums.PaperType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResearchPaper implements Serializable {
    private static final long serialVersionUID = 1L;

    private int paperId;
    private String title;
    private int citations;
    private int pages;
    private String journal;   // journal name
    private String doi;
    private Date datePublished;
    private List<String> authorNames;
    private PaperType type;

    public ResearchPaper(int paperId, String title, String journal, int pages, String doi) {
        this.paperId = paperId;
        this.title = title;
        this.journal = journal;
        this.pages = pages;
        this.doi = doi;
        this.citations = 0;
        this.datePublished = new Date();
        this.authorNames = new ArrayList<>();
    }

    public int getPaperId()              { return paperId; }
    public String getTitle()             { return title; }
    public int getCitations()            { return citations; }
    public int getPages()                { return pages; }
    public String getJournal()           { return journal; }
    public String getDoi()               { return doi; }
    public Date getDatePublished()       { return datePublished; }
    public List<String> getAuthorNames() { return authorNames; }
    public PaperType getType()           { return type; }

    public void setTitle(String title)   { this.title = title; }
    public void setJournal(String j)     { this.journal = j; }
    public void setDoi(String doi)       { this.doi = doi; }
    public void setType(PaperType type)  { this.type = type; }

    public void addAuthorName(String name) {
        authorNames.add(name);
    }

    /**
     * Returns citation in Plain Text or BibTeX format (Requirement).
     */
    @SuppressWarnings("deprecation")
    public String getCitation(CitationFormat format) {
        String authorsStr = authorNames.isEmpty() ? "Unknown" : String.join(", ", authorNames);
        int year = 1900 + datePublished.getYear();
        String citation;

        if (format == CitationFormat.BIBTEX) {
            String key = (authorNames.isEmpty() ? "unknown" :
                    authorNames.get(0).replaceAll("\\s+", "")) + year;
            citation = "@article{" + key + ",\n"
                    + "  author  = {" + authorsStr + "},\n"
                    + "  title   = {" + title + "},\n"
                    + "  journal = {" + journal + "},\n"
                    + "  year    = {" + year + "},\n"
                    + "  pages   = {" + pages + "},\n"
                    + "  doi     = {" + doi + "}\n"
                    + "}";
        } else {
            // PLAIN_TEXT
            citation = authorsStr + " (" + year + "). " + title
                    + ". " + journal + ". Pages: " + pages
                    + ". DOI: " + doi;
        }

        System.out.println("[Citation - " + format + "]\n" + citation);
        return citation;
    }

    /** Legacy string-based overload kept for backward compatibility */
    public String getCitation(String format) {
        CitationFormat f = format.equalsIgnoreCase("BIBTEX") || format.equalsIgnoreCase("Bibtex")
                ? CitationFormat.BIBTEX : CitationFormat.PLAIN_TEXT;
        return getCitation(f);
    }

    public void addCite() {
        citations++;
        System.out.println("[ResearchPaper] '" + title + "' cited. Total: " + citations);
    }

    @Override
    public String toString() {
        return "ResearchPaper{id=" + paperId + ", title='" + title
                + "', journal='" + journal + "', citations=" + citations
                + ", pages=" + pages + ", doi='" + doi + "'}";
    }
}