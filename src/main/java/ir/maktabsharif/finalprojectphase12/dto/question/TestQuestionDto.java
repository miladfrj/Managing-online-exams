package ir.maktabsharif.finalprojectphase12.dto.question;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestQuestionDto {
    private Long questionId;
    private String questionTitle;
    private String questionText;
}
