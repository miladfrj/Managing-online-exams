package ir.maktabsharif.finalprojectphase12.entity;

import ir.maktabsharif.finalprojectphase12.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student_tests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentTest extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    private Integer remainingTime;
    private boolean completed;
    private Double totalScore;

    @OneToMany(mappedBy = "studentTest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentAnswer> answers = new ArrayList<>();



    public Long getCourseId() {
        return test != null ? test.getCourse().getId() : null;
    }
}
