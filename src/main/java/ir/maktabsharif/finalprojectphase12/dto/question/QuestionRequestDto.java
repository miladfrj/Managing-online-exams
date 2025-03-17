package ir.maktabsharif.finalprojectphase12.dto.question;

import ir.maktabsharif.finalprojectphase12.entity.question.QuestionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;



/*@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Question text is required")
    private String questionText;

    @NotNull(message = "Default score is required")
    @Min(value = 0, message = "Default score must be a positive number")
    private Double defaultScore;

    @NotNull(message = "Question type is required")
    private QuestionType questionType;

    private List<String> options; // For MCQ questions

    private String correctAnswer; // For MCQ questions

    @NotNull(message = "Course ID is required")
    private Long courseId;
}*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Question text is required")
    private String questionText;

    @NotNull(message = "Default score is required")
    @Min(value = 0, message = "Default score must be a positive number")
    private Double defaultScore;

    @NotNull(message = "Question type is required")
    private QuestionType questionType;

    private List<String> options; // For MCQ questions

    private String correctAnswer; // For MCQ questions

    @NotNull(message = "Course ID is required")
    private Long courseId;
}




