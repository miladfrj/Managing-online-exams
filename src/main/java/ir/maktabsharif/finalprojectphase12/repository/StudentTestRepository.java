package ir.maktabsharif.finalprojectphase12.repository;


import ir.maktabsharif.finalprojectphase12.entity.StudentTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentTestRepository extends JpaRepository<StudentTest, Long> {
    // prevent duplicate test participants
    Optional<StudentTest> findByStudentIdAndTestId(Long studentId, Long testId);
    List<StudentTest> findAllByTestId(Long testId);
}