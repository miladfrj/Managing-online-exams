package ir.maktabsharif.finalprojectphase12.service;


import ir.maktabsharif.finalprojectphase12.dto.question.DescriptiveQuestionRequestDto;
import ir.maktabsharif.finalprojectphase12.dto.question.MultiQuestionDto;
import ir.maktabsharif.finalprojectphase12.dto.question.TestQuestionDto;
import ir.maktabsharif.finalprojectphase12.entity.question.DescriptiveQuestion;
import ir.maktabsharif.finalprojectphase12.entity.question.MultipleChoiceQuestion;
import ir.maktabsharif.finalprojectphase12.entity.question.Question;
import ir.maktabsharif.finalprojectphase12.entity.question.TestQuestion;
import jakarta.transaction.Transactional;

import java.util.List;


public interface QuestionService {

    void createDescriptiveQuestion(Long testId, DescriptiveQuestion question, Long teacherId);

    void createMultipleChoiceQuestion(Long testId, MultipleChoiceQuestion question, List<String> options,
                                      int correctAnswer, Long teacherId);

    void addQuestionFromBank(Long examId, Long questionId);

   // void updateQuestionScore(Long questionId, Double score, Long questionIdReal);

    void updateQuestionScore(Long testQuestionId, Double score);

    void updateDescriptiveQuestion(Long examId, TestQuestionDto examQuestionDto);

    void updateMultipleChoiceQuestion(Long examId, MultipleChoiceQuestion multipleChoiceQuestion);

    void deleteQuestionById(Long questionId, Long examId);

    /*List<Question> getQuestionsByTestId(Long testId);*/

    List<Question> getQuestionsByTestId(Long testId);

    List<Question> getQuestionsByTeacherIdAndCourseId(Long teacherId, Long courseId);

    List<Question> getQuestionsByTeacherId(Long teacherId);

    Question getQuestionById(Long questionId);

    TestQuestion findTestQuestionById(Long questionId);

}



