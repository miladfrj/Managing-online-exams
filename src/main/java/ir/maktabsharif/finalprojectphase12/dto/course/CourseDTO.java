package ir.maktabsharif.finalprojectphase12.dto.course;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Long id;
    private String courseCode;
    private String title;
    private LocalDate startDate; // Use LocalDate
    private LocalDate endDate;
    private Long teacherId;
    private List<Long> studentIds;

}
