package ir.maktabsharif.finalprojectphase12.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultiQuestionDto {
    private Long id;
    private String questionTitle;
    private String questionText;
    private List<String> options;
    private String correctAnswer;
}