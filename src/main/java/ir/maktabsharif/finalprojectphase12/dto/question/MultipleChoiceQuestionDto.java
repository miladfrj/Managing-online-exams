package ir.maktabsharif.finalprojectphase12.dto.question;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultipleChoiceQuestionDto {
    private Long id;
    private String title;
    private String questionText;
    private List<String> options;
    private String correctAnswer;
}


