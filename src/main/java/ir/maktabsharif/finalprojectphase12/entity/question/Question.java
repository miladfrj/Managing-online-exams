package ir.maktabsharif.finalprojectphase12.entity.question;

import ir.maktabsharif.finalprojectphase12.entity.Course;
import ir.maktabsharif.finalprojectphase12.entity.Test;
import ir.maktabsharif.finalprojectphase12.entity.User;
import ir.maktabsharif.finalprojectphase12.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;



@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "question_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Question extends BaseEntity {

    @Column(nullable = false)
    private String title; // Mandatory title for the question

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText; // The actual question text

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher; // The teacher who created the question

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // The course this question belongs to


    // Helper method to get the question type
    public String getQuestionType() {
        DiscriminatorValue discriminatorValue = this.getClass().getAnnotation(DiscriminatorValue.class);
        return discriminatorValue != null ? discriminatorValue.value() : "UNKNOWN";
    }
}



/*
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Question extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(nullable = false)
    private Double defaultScore;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option")
    private List<String> options;

    private String correctAnswer;

}*/
