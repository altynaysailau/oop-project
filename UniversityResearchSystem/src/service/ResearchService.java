package service;
import java.util.List;

import model.ResearchPaper;
import model.Researcher;
public class ResearchService {

    public static int calculateHIndex(Researcher researcher) {
        List<ResearchPaper> papers = researcher.getPapers();
        if (papers.isEmpty()) return 0;

        papers.sort((p1, p2) -> Integer.compare(p2.getCitations(), p1.getCitations()));

        int h = 0;
        for (int i = 0; i < papers.size(); i++) {
            if (papers.get(i).getCitations() >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }
        return h;
    }
}