package ir.maktabsharif.finalprojectphase12.entity;

import ir.maktabsharif.finalprojectphase12.entity.base.BaseEntity;
import ir.maktabsharif.finalprojectphase12.entity.question.Question;
import ir.maktabsharif.finalprojectphase12.entity.question.TestQuestion;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;




@Entity
@Table(name = "student_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnswer extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "student_test_id", nullable = false)
    private StudentTest studentTest;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = true)
    private String answer;

    @Column
    private Double score;

    @Column
    private String feedback;

    private boolean descriptiveQuestion;
}
