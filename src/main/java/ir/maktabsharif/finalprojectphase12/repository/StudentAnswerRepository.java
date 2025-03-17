package ir.maktabsharif.finalprojectphase12.repository;

import ir.maktabsharif.finalprojectphase12.entity.StudentAnswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer,Long> {
  /*  @Query("SELECT sa FROM StudentAnswer sa WHERE sa.studentTest.id = :studentTestId")
    List<StudentAnswer> findByStudentTestId(Long studentTestId); // Get answers for a test session*/

    List<StudentAnswer> findByQuestionId(Long questionId);
}

