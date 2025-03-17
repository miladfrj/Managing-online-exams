package ir.maktabsharif.finalprojectphase12.dto.test;

import ir.maktabsharif.finalprojectphase12.entity.question.TestQuestion;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestDTO {
    private Long id;
    private String title;
    private String description;
    private Integer time;
    private Long courseId;
    private Long teacherId;
    private List<TestQuestion> testQuestions;
}

