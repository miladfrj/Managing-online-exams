package ir.maktabsharif.finalprojectphase12.repository.question;

import ir.maktabsharif.finalprojectphase12.entity.StudentTest;
import ir.maktabsharif.finalprojectphase12.entity.question.TestQuestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {
    List<TestQuestion> findByTestId(Long testId);
    //which tests a particular question has been used.
   // List<TestQuestion> findTestQuestionByQuestionId(Long questionId);
   // void deleteByQuestionId(Long questionId);

   // List<TestQuestion> findByQuestionId(Long questionId);
}
