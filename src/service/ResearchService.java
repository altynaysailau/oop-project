package service;

import model.ResearchPaper;
import model.Researcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResearchService {

    public static int calculateHIndex(Researcher researcher) {
        List<ResearchPaper> papers = new ArrayList<>(researcher.getPapers());
        if (papers.isEmpty()) return 0;
        papers.sort((p1, p2) -> Integer.compare(p2.getCitations(), p1.getCitations()));
        int h = 0;
        for (int i = 0; i < papers.size(); i++) {
            if (papers.get(i).getCitations() >= i + 1) h = i + 1;
            else break;
        }
        return h;
    }

    /** Print all papers across all researchers sorted by given comparator */
    public static void printAllPapers(List<Researcher> researchers,
                                      Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> all = new ArrayList<>();
        for (Researcher r : researchers) all.addAll(r.getPapers());
        all.sort(comparator);
        System.out.println("=== All Research Papers ===");
        for (ResearchPaper p : all) System.out.println("  " + p);
    }

    /** Print top cited researcher of the year across all schools */
    public static void printTopCitedResearcher(List<Researcher> researchers) {
        researchers.stream()
            .max(Comparator.comparingInt(r ->
                r.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum()))
            .ifPresent(top -> {
                int total = top.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum();
                System.out.println(" Top cited researcher: "
                    + top.getFirstName() + " " + top.getLastName()
                    + " with " + total + " total citations (h="
                    + calculateHIndex(top) + ")");
            });
    }
}