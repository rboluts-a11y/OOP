package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class StudentRecordPossibilitiesTest {
    private final double EPS = 0.001;

    private static final int[] DIFFERENTIATED_CREDITS_PER_SEMESTER =
            {3, 3, 6, 5, 4, 6, 4, 4};

    private static final int[] EXAMS_PER_SEMESTER =
            {3, 3, 2, 5, 3, 2, 1, 0};

    private static final int[] CREDITS_PER_SEMESTER =
            {3, 2, 0, 0, 0, 0, 1, 1};

    private Grade[] grades(int count, Grade grade) {
        Grade[] result = new Grade[count];
        Arrays.fill(result, grade);
        return result;
    }

    private StudentRecord createFullStudyPlanRecord(
            StudentRecord.StudyForm studyForm,
            int currentSemester,
            Grade[] differentiatedGrades,
            Grade[] examGrades,
            Grade[] creditGrades,
            Grade qualificationWorkGrade
    ) {
        if (differentiatedGrades.length != 35
                || examGrades.length != 19
                || creditGrades.length != 7) {
            throw new IllegalArgumentException("Неправильное количество оценок");
        }
        StudentRecord record = createRecord(
                studyForm,
                8,
                currentSemester
        );
        int differentiatedIndex = 0;
        int examIndex = 0;
        int creditIndex = 0;
        for (int semester = 1; semester <= 8; semester++) {
            for (int i = 1; i <= DIFFERENTIATED_CREDITS_PER_SEMESTER[semester - 1]; i++) {
                addSubject(
                        record,
                        semester,
                        "S" + semester + " Дифф. зачёт " + i,
                        Subject.AssessmentType.DIFFERENTIATED_CREDIT,
                        differentiatedGrades[differentiatedIndex++]
                );
            }
            for (int i = 1; i <= EXAMS_PER_SEMESTER[semester - 1]; i++) {
                addSubject(
                        record,
                        semester,
                        "S" + semester + " Экзамен " + i,
                        Subject.AssessmentType.EXAM,
                        examGrades[examIndex++]
                );
            }

            for (int i = 1; i <= CREDITS_PER_SEMESTER[semester - 1]; i++) {
                addSubject(
                        record,
                        semester,
                        "S" + semester + " Зачёт " + i,
                        Subject.AssessmentType.CREDIT,
                        creditGrades[creditIndex++]
                );
            }
        }
        if (qualificationWorkGrade != null) {
            record.addQualificationWorkGrade(qualificationWorkGrade);
        }
        return record;
    }

    private StudentRecord createRecord(
            StudentRecord.StudyForm studyForm,
            int numberOfSemesters,
            int currentSemesterNumber
    ) {
        StudentRecord record = new StudentRecord(studyForm);
        for (int i = 0; i < numberOfSemesters; i++) {
            record.addSemester();
        }
        record.setCurrentSemesterNumber(currentSemesterNumber);
        return record;
    }

    private void addSubject(
            StudentRecord record,
            int semesterNumber,
            String name,
            Subject.AssessmentType assessmentType,
            Grade... grades
    ) {
        record.addSubject(
                semesterNumber,
                new Subject(name, assessmentType)
        );
        for (Grade grade : grades) {
            record.addGrade(semesterNumber, name, grade);
        }
    }

    private void addSubjectsWithGrade(
            StudentRecord record,
            int semesterNumber,
            String namePrefix,
            Subject.AssessmentType assessmentType,
            Grade grade,
            int count
    ) {
        for (int i = 1; i <= count; i++) {
            addSubject(record, semesterNumber, namePrefix + i, assessmentType, grade);
        }
    }

    @Test
    void emptyRecordHasNoAverageButAllowsFutureResults() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.PAID, 1, 1);
        assertEquals(0.0, record.getAverageScore(), EPS);
        assertFalse(record.canTransferToBudget());
        assertTrue(record.canGetRedDiploma());
        assertTrue(record.canGetIncreasedScholarship());
    }

    @Test
    void averageUsesLastGradesAndIgnoresOrdinaryCredits() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.PAID, 2, 2);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.SATISFACTORY,
                Grade.EXCELLENT
        );
        addSubject(
                record, 1, "Физика",
                Subject.AssessmentType.DIFFERENTIATED_CREDIT,
                Grade.GOOD
        );
        addSubject(
                record, 1, "Физкультура",
                Subject.AssessmentType.CREDIT,
                Grade.PASS
        );
        addSubject(
                record, 2, "Программирование",
                Subject.AssessmentType.EXAM,
                Grade.GOOD
        );
        addSubject(
                record, 2, "История",
                Subject.AssessmentType.CREDIT,
                Grade.PASS
        );
        assertEquals(4.33, record.getAverageScore(), EPS);
        assertTrue(record.canTransferToBudget());
        assertTrue(record.canGetRedDiploma());
        assertFalse(record.canGetIncreasedScholarship());
    }

    @Test
    void ordinaryCreditsDoNotAffectAverageOrScholarship() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.BUDGET, 1, 1);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.EXCELLENT
        );
        addSubject(
                record, 1, "Физика",
                Subject.AssessmentType.DIFFERENTIATED_CREDIT,
                Grade.GOOD
        );
        addSubject(
                record, 1, "Физкультура",
                Subject.AssessmentType.CREDIT,
                Grade.PASS
        );
        assertEquals(4.5, record.getAverageScore(), EPS);
        assertFalse(record.canTransferToBudget());
        assertTrue(record.canGetRedDiploma());
        assertTrue(record.canGetIncreasedScholarship());
    }

    @Test
    void budgetStudentCannotTransferToBudgetAgain() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.BUDGET, 2, 2);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.EXCELLENT
        );
        addSubject(
                record, 2, "Программирование",
                Subject.AssessmentType.EXAM,
                Grade.EXCELLENT
        );
        assertEquals(5.0, record.getAverageScore(), EPS);
        assertFalse(record.canTransferToBudget());
        assertTrue(record.canGetRedDiploma());
        assertTrue(record.canGetIncreasedScholarship());
    }

    @Test
    void paidStudentCannotTransferBeforeSecondSemester() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.PAID, 1, 1);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.EXCELLENT
        );
        assertEquals(5.0, record.getAverageScore(), EPS);
        assertFalse(record.canTransferToBudget());
        assertTrue(record.canGetRedDiploma());
        assertTrue(record.canGetIncreasedScholarship());
    }

    @Test
    void differentiatedCreditWithGradeThreeDoesNotPreventTransfer() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.PAID, 2, 2);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.GOOD
        );
        addSubject(
                record, 2, "Физика",
                Subject.AssessmentType.DIFFERENTIATED_CREDIT,
                Grade.SATISFACTORY
        );
        addSubject(
                record, 2, "Программирование",
                Subject.AssessmentType.EXAM,
                Grade.GOOD
        );
        assertEquals(3.67, record.getAverageScore(), EPS);
        assertTrue(record.canTransferToBudget());
        assertFalse(record.canGetRedDiploma());
        assertFalse(record.canGetIncreasedScholarship());
    }

    @Test
    void examWithGradeThreePreventsTransfer() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.PAID, 2, 2);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.SATISFACTORY
        );
        addSubject(
                record, 2, "Программирование",
                Subject.AssessmentType.EXAM,
                Grade.EXCELLENT
        );
        assertEquals(4.0, record.getAverageScore(), EPS);
        assertFalse(record.canTransferToBudget());
        assertFalse(record.canGetRedDiploma());
        assertFalse(record.canGetIncreasedScholarship());
    }

    @Test
    void transferChecksOnlyTwoLastSemesters() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.PAID, 3, 3);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.SATISFACTORY
        );
        addSubject(
                record, 2, "Физика",
                Subject.AssessmentType.EXAM,
                Grade.GOOD
        );
        addSubject(
                record, 3, "Программирование",
                Subject.AssessmentType.EXAM,
                Grade.EXCELLENT
        );
        assertEquals(4.0, record.getAverageScore(), EPS);
        assertTrue(record.canTransferToBudget());
        assertFalse(record.canGetRedDiploma());
        assertTrue(record.canGetIncreasedScholarship());
    }

    @Test
    void unresolvedFailInOrdinaryCreditPreventsTransferAndRedDiploma() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.PAID, 2, 2);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.EXCELLENT
        );
        addSubject(
                record, 2, "Физкультура",
                Subject.AssessmentType.CREDIT,
                Grade.FAIL
        );
        assertEquals(5.0, record.getAverageScore(), EPS);
        assertFalse(record.canTransferToBudget());
        assertFalse(record.canGetRedDiploma());
        assertFalse(record.canGetIncreasedScholarship());
    }

    @Test
    void retakeUsesLastGradeButPreventsIncreasedScholarship() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.PAID, 2, 2);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.SATISFACTORY,
                Grade.EXCELLENT
        );
        addSubject(
                record, 2, "Физика",
                Subject.AssessmentType.DIFFERENTIATED_CREDIT,
                Grade.GOOD
        );
        assertEquals(4.5, record.getAverageScore(), EPS);
        assertTrue(record.canTransferToBudget());
        assertTrue(record.canGetRedDiploma());
        assertFalse(record.canGetIncreasedScholarship());
    }

    @Test
    void scholarshipInFirstSemesterChecksOnlyFirstSemester() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.BUDGET, 2, 1);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.EXCELLENT
        );
        addSubject(
                record, 2, "Физика",
                Subject.AssessmentType.EXAM,
                Grade.SATISFACTORY
        );
        assertEquals(4.0, record.getAverageScore(), EPS);
        assertFalse(record.canTransferToBudget());
        assertFalse(record.canGetRedDiploma());
        assertTrue(record.canGetIncreasedScholarship());
    }

    @Test
    void scholarshipInLaterSemesterChecksOnlyTwoLastSemesters() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.PAID, 3, 3);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.SATISFACTORY
        );
        addSubject(
                record, 2, "Физика",
                Subject.AssessmentType.DIFFERENTIATED_CREDIT,
                Grade.GOOD
        );
        addSubject(
                record, 3, "Программирование",
                Subject.AssessmentType.EXAM,
                Grade.EXCELLENT
        );
        assertEquals(4.0, record.getAverageScore(), EPS);
        assertTrue(record.canTransferToBudget());
        assertFalse(record.canGetRedDiploma());
        assertTrue(record.canGetIncreasedScholarship());
    }

    @Test
    void scholarshipIsUnavailableAfterRetakeInLastTwoSemesters() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.PAID, 2, 2);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.GOOD,
                Grade.EXCELLENT
        );
        addSubject(
                record, 2, "Физика",
                Subject.AssessmentType.DIFFERENTIATED_CREDIT,
                Grade.GOOD
        );
        assertEquals(4.5, record.getAverageScore(), EPS);
        assertTrue(record.canTransferToBudget());
        assertTrue(record.canGetRedDiploma());
        assertFalse(record.canGetIncreasedScholarship());
    }

    @Test
    void scholarshipIsUnavailableWithGradeBelowFourInLastTwoSemesters() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.PAID, 2, 2);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.GOOD
        );
        addSubject(
                record, 2, "Физика",
                Subject.AssessmentType.EXAM,
                Grade.SATISFACTORY
        );
        assertEquals(3.5, record.getAverageScore(), EPS);
        assertFalse(record.canTransferToBudget());
        assertFalse(record.canGetRedDiploma());
        assertFalse(record.canGetIncreasedScholarship());
    }

    @Test
    void redDiplomaIsImpossibleWithAnyFinalGradeBelowFour() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.BUDGET, 1, 1);
        addSubjectsWithGrade(
                record, 1, "Дифференцированный зачёт ",
                Subject.AssessmentType.DIFFERENTIATED_CREDIT,
                Grade.EXCELLENT,
                35
        );
        addSubjectsWithGrade(
                record, 1, "Экзамен — отлично ",
                Subject.AssessmentType.EXAM,
                Grade.EXCELLENT,
                18
        );
        addSubject(
                record, 1, "Последний экзамен",
                Subject.AssessmentType.EXAM,
                Grade.SATISFACTORY
        );
        record.addQualificationWorkGrade(Grade.EXCELLENT);
        assertEquals(4.96, record.getAverageScore(), EPS);
        assertFalse(record.canTransferToBudget());
        assertFalse(record.canGetRedDiploma());
        assertFalse(record.canGetIncreasedScholarship());
    }

    @Test
    void redDiplomaRequiresExcellentQualificationWork() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.BUDGET, 1, 1);
        addSubjectsWithGrade(
                record, 1, "Дифференцированный зачёт — отлично ",
                Subject.AssessmentType.DIFFERENTIATED_CREDIT,
                Grade.EXCELLENT,
                30
        );
        addSubjectsWithGrade(
                record, 1, "Дифференцированный зачёт — хорошо ",
                Subject.AssessmentType.DIFFERENTIATED_CREDIT,
                Grade.GOOD,
                5
        );
        addSubjectsWithGrade(
                record, 1, "Экзамен — отлично ",
                Subject.AssessmentType.EXAM,
                Grade.EXCELLENT,
                11
        );
        addSubjectsWithGrade(
                record, 1, "Экзамен — хорошо ",
                Subject.AssessmentType.EXAM,
                Grade.GOOD,
                8
        );
        record.addQualificationWorkGrade(Grade.GOOD);
        assertEquals(4.76, record.getAverageScore(), EPS);
        assertFalse(record.canTransferToBudget());
        assertFalse(record.canGetRedDiploma());
        assertTrue(record.canGetIncreasedScholarship());
    }

    @Test
    void failInOrdinaryCreditDoesNotAffectAverageButPreventsRedDiplomaAndScholarship() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.PAID, 2, 2);
        addSubjectsWithGrade(
                record, 1, "Дифференцированный зачёт ",
                Subject.AssessmentType.DIFFERENTIATED_CREDIT,
                Grade.EXCELLENT,
                35
        );
        addSubjectsWithGrade(
                record, 1, "Экзамен ",
                Subject.AssessmentType.EXAM,
                Grade.EXCELLENT,
                19
        );
        addSubject(
                record, 2, "Физкультура",
                Subject.AssessmentType.CREDIT,
                Grade.FAIL
        );
        record.addQualificationWorkGrade(Grade.EXCELLENT);
        assertEquals(5.0, record.getAverageScore(), EPS);
        assertFalse(record.canTransferToBudget());
        assertFalse(record.canGetRedDiploma());
        assertFalse(record.canGetIncreasedScholarship());
    }

    @Test
    void failInGradedSubjectAffectsAllFourResults() {
        StudentRecord record = createRecord(StudentRecord.StudyForm.PAID, 2, 2);
        addSubject(
                record, 1, "Математический анализ",
                Subject.AssessmentType.EXAM,
                Grade.FAIL
        );
        addSubject(
                record, 2, "Физика",
                Subject.AssessmentType.EXAM,
                Grade.EXCELLENT
        );
        assertEquals(3.5, record.getAverageScore(), EPS);
        assertFalse(record.canTransferToBudget());
        assertFalse(record.canGetRedDiploma());
        assertFalse(record.canGetIncreasedScholarship());
    }

    @Test
    void transferAndScholarshipRequireConfiguredCurrentSemester() {
        StudentRecord record = new StudentRecord(StudentRecord.StudyForm.PAID);
        record.addSemester();
        assertEquals(0.0, record.getAverageScore(), EPS);
        assertThrows(
                IllegalStateException.class,
                record::canTransferToBudget
        );
        assertTrue(record.canGetRedDiploma());
        assertThrows(
                IllegalStateException.class,
                record::canGetIncreasedScholarship
        );
    }

    @Test
    void redDiplomaIsPossibleAtMinimumExcellentGradeRatio() {
        Grade[] differentiatedGrades = grades(35, Grade.GOOD);
        Grade[] examGrades = grades(19, Grade.GOOD);
        Grade[] creditGrades = grades(7, Grade.PASS);
        for (int i = 0; i < 30; i++) {
            differentiatedGrades[i] = Grade.EXCELLENT;
        }
        for (int i = 0; i < 11; i++) {
            examGrades[i] = Grade.EXCELLENT;
        }
        StudentRecord record = createFullStudyPlanRecord(
                StudentRecord.StudyForm.BUDGET,
                8,
                differentiatedGrades,
                examGrades,
                creditGrades,
                Grade.EXCELLENT
        );
        assertEquals(4.76, record.getAverageScore(), 0.001);
        assertFalse(record.canTransferToBudget());
        assertTrue(record.canGetRedDiploma());
        assertTrue(record.canGetIncreasedScholarship());
    }

    @Test
    void redDiplomaIsImpossibleBelowMinimumExcellentGradeRatio() {
        Grade[] differentiatedGrades = grades(35, Grade.GOOD);
        Grade[] examGrades = grades(19, Grade.GOOD);
        Grade[] creditGrades = grades(7, Grade.PASS);
        for (int i = 0; i < 29; i++) {
            differentiatedGrades[i] = Grade.EXCELLENT;
        }
        for (int i = 0; i < 11; i++) {
            examGrades[i] = Grade.EXCELLENT;
        }
        StudentRecord record = createFullStudyPlanRecord(
                StudentRecord.StudyForm.BUDGET,
                8,
                differentiatedGrades,
                examGrades,
                creditGrades,
                Grade.EXCELLENT
        );
        assertEquals(4.74, record.getAverageScore(), 0.001);
        assertFalse(record.canTransferToBudget());
        assertFalse(record.canGetRedDiploma());
        assertTrue(record.canGetIncreasedScholarship());
    }

    @Test
    void failInLastOrdinaryCreditPreventsTransferAndRedDiploma() {
        Grade[] differentiatedGrades = grades(35, Grade.EXCELLENT);
        Grade[] examGrades = grades(19, Grade.EXCELLENT);
        Grade[] creditGrades = grades(7, Grade.PASS);
        creditGrades[6] = Grade.FAIL;
        StudentRecord record = createFullStudyPlanRecord(
                StudentRecord.StudyForm.PAID,
                8,
                differentiatedGrades,
                examGrades,
                creditGrades,
                Grade.EXCELLENT
        );
        assertEquals(5.0, record.getAverageScore(), 0.001);
        assertFalse(record.canTransferToBudget());
        assertFalse(record.canGetRedDiploma());
        assertFalse(record.canGetIncreasedScholarship());
    }
}