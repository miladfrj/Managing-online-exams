package ir.maktabsharif.finalprojectphase12.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerReviewDTO {
    private Long answerId;
    private String questionText;
    private String studentAnswer;
    private Double score;
    private Double maxScore;
    private String feedback;
    private boolean descriptiveQuestion;
    private int questionIndex;
}
