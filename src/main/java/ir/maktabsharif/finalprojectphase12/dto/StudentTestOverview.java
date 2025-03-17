package ir.maktabsharif.finalprojectphase12.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentTestOverview {
    private Long studentTestId;
    private String studentName;
    private boolean completed;
    private Double totalScore;
}