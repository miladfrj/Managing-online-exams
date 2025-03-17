package ir.maktabsharif.finalprojectphase12.entity.question;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("DESCRIPTION")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class DescriptiveQuestion extends Question {
    // No additional fields needed for descriptive questions
}
