package ir.maktabsharif.finalprojectphase12.dto.question;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DescriptiveQuestionRequestDto {
    private Long id; // For editing
    private String title; // Question title
    private String questionText; // Question text
}