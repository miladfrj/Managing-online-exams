package ir.maktabsharif.finalprojectphase12.repository.question;

import ir.maktabsharif.finalprojectphase12.entity.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTeacherIdAndCourseId(Long teacherId, Long courseId);

    List<Question> findByTeacherId(Long teacherId);
       /* @Query("SELECT q FROM Question q WHERE q.course.id = :courseId AND q.teacher.id = :teacherId AND (q.exam IS NULL OR q.exam.id <> :examId)")
        List<Question> findByCourseIdAndTeacherIdAndExamIdNot(@Param("courseId") Long courseId,
                                                              @Param("teacherId") Long teacherId,
                                                             @Param("examId") Long examId);*/
}


