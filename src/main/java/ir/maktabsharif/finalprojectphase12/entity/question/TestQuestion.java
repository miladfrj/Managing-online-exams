package ir.maktabsharif.finalprojectphase12.entity.question;

import ir.maktabsharif.finalprojectphase12.entity.Test;
import ir.maktabsharif.finalprojectphase12.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TestQuestion extends BaseEntity {

        @ManyToOne
        @JoinColumn(name = "test_id", nullable = false)
        private Test test; // The test this question belongs to

        @ManyToOne
        @JoinColumn(name = "question_id", referencedColumnName = "id", nullable = false)
        private Question question; // The question

        @Column(nullable = false)
        private Double score;
}



