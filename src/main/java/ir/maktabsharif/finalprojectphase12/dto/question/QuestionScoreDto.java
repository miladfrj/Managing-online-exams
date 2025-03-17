package ir.maktabsharif.finalprojectphase12.dto.question;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*@Getter
@Setter
public class QuestionScoreDto {
    private List<Long> questionIds;
    private List<Double> scores;
    private List<Long> questionIdReal;
}*/

@Getter
@Setter
public class QuestionScoreDto {
    private List<Long> testQuestionIds; // IDs of TestQuestion entities
    private List<Double> scores;        // Scores for each TestQuestion
}
