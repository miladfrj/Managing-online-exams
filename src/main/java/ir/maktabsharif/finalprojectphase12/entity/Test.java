package ir.maktabsharif.finalprojectphase12.entity;

import ir.maktabsharif.finalprojectphase12.entity.base.BaseEntity;
import ir.maktabsharif.finalprojectphase12.entity.question.Question;
import ir.maktabsharif.finalprojectphase12.entity.question.TestQuestion;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Test extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private int time;


    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false, updatable = false)
    private User teacher;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestQuestion> testQuestions = new ArrayList<>();

}
