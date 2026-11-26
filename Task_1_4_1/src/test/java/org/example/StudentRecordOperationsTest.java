package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

public class StudentRecordOperationsTest {
    private StudentRecord createRecord() {
        StudentRecord record = new StudentRecord(StudentRecord.StudyForm.BUDGET);
        record.addSemester();
        record.addSemester();
        record.addSemester();
        record.setCurrentSemesterNumber(1);
        return record;
    }

    private Subject createSubject(String name) {
        return new Subject(name, Subject.AssessmentType.EXAM);
    }

    @Test
    void constructorCreatesEmptyRecord() {
        StudentRecord record = new StudentRecord(StudentRecord.StudyForm.BUDGET);
        assertEquals(StudentRecord.StudyForm.BUDGET, record.getStudyForm());
        assertEquals(0, record.getSemesters().size());
        assertEquals(0, record.getCurrentSemesterNumber());
        assertNull(record.getQualificationWorkGrade());
    }

    @Test
    void changeStudyForm() {
        StudentRecord record = createRecord();
        record.setStudyForm(StudentRecord.StudyForm.PAID);
        assertEquals(StudentRecord.StudyForm.PAID, record.getStudyForm());
    }

    @Test
    void addSemester() {
        StudentRecord record = new StudentRecord(StudentRecord.StudyForm.BUDGET);
        record.addSemester();
        assertEquals(1, record.getSemesters().size());
        record.addSemester();
        assertEquals(2, record.getSemesters().size());
    }

    @Test
    void removeLastSemester() {
        StudentRecord record = createRecord();
        record.removeLastSemester();
        assertEquals(2, record.getSemesters().size());
    }

    @Test
    void removeLastSemesterUpdatesCurrentSemesterNumber() {
        StudentRecord record = createRecord();
        record.setCurrentSemesterNumber(3);
        record.removeLastSemester();
        assertEquals(2, record.getSemesters().size());
        assertEquals(2, record.getCurrentSemesterNumber());
    }

    @Test
    void removeLastSemesterWhenNoSemestersExist() {
        StudentRecord record = new StudentRecord(StudentRecord.StudyForm.BUDGET);
        assertThrows(
                IllegalStateException.class,
                record::removeLastSemester
        );
    }

    @Test
    void setCurrentSemesterNumber() {
        StudentRecord record = createRecord();
        record.setCurrentSemesterNumber(2);
        assertEquals(2, record.getCurrentSemesterNumber());
    }

    @Test
    void setCurrentSemesterNumberToZero() {
        StudentRecord record = createRecord();
        assertThrows(
                IllegalArgumentException.class,
                () -> record.setCurrentSemesterNumber(0)
        );
    }

    @Test
    void setCurrentSemesterNumberBeyondExistingSemesters() {
        StudentRecord record = createRecord();
        assertThrows(
                IllegalArgumentException.class,
                () -> record.setCurrentSemesterNumber(4)
        );
    }

    @Test
    void addSubject() {
        StudentRecord record = createRecord();
        Subject subject = createSubject("Математический анализ");
        record.addSubject(1, subject);
        assertEquals(
                1,
                record.getSemesters().get(0).getSubjects().size()
        );
        assertEquals(
                "Математический анализ",
                record.getSemesters().get(0).getSubjects().get(0).getName()
        );
    }

    @Test
    void addSubjectToDifferentSemesters() {
        StudentRecord record = createRecord();
        record.addSubject(1, createSubject("Английский язык"));
        record.addSubject(2, createSubject("Английский язык"));
        assertEquals(1, record.getSemesters().get(0).getSubjects().size());
        assertEquals(1, record.getSemesters().get(1).getSubjects().size());
    }

    @Test
    void addDuplicateSubjectToSameSemester() {
        StudentRecord record = createRecord();
        record.addSubject(1, createSubject("Английский язык"));
        assertThrows(
                IllegalArgumentException.class,
                () -> record.addSubject(1, createSubject("Английский язык"))
        );
    }

    @Test
    void addSubjectToNonexistentSemester() {
        StudentRecord record = createRecord();
        assertThrows(
                IllegalArgumentException.class,
                () -> record.addSubject(4, createSubject("Физика"))
        );
    }

    @Test
    void addSubjectToSemesterZero() {
        StudentRecord record = createRecord();
        assertThrows(
                IllegalArgumentException.class,
                () -> record.addSubject(0, createSubject("Физика"))
        );
    }

    @Test
    void removeSubject() {
        StudentRecord record = createRecord();
        record.addSubject(1, createSubject("Физика"));
        record.removeSubject(1, "Физика");
        assertTrue(record.getSemesters().get(0).getSubjects().isEmpty());
    }

    @Test
    void removeNonexistentSubject() {
        StudentRecord record = createRecord();
        assertThrows(
                IllegalArgumentException.class,
                () -> record.removeSubject(1, "Физика")
        );
    }

    @Test
    void removeSubjectFromNonexistentSemester() {
        StudentRecord record = createRecord();
        assertThrows(
                IllegalArgumentException.class,
                () -> record.removeSubject(4, "Физика")
        );
    }

    @Test
    void addGradeToSubject() {
        StudentRecord record = createRecord();
        record.addSubject(1, createSubject("Физика"));
        record.addGrade(1, "Физика", Grade.GOOD);
        assertEquals(
                Grade.GOOD,
                record.getSemesters().get(0).getSubjects().get(0).getLastGrade()
        );
    }

    @Test
    void addSeveralGradesToSubject() {
        StudentRecord record = createRecord();
        record.addSubject(1, createSubject("Физика"));
        record.addGrade(1, "Физика", Grade.SATISFACTORY);
        record.addGrade(1, "Физика", Grade.GOOD);
        assertEquals(
                List.of(Grade.SATISFACTORY, Grade.GOOD),
                record.getSemesters().get(0).getSubjects().get(0).getHistory()
        );
        assertEquals(
                Grade.GOOD,
                record.getSemesters().get(0).getSubjects().get(0).getLastGrade()
        );
    }

    @Test
    void addGradeToNonexistentSemester() {
        StudentRecord record = createRecord();
        assertThrows(
                IllegalArgumentException.class,
                () -> record.addGrade(4, "Физика", Grade.EXCELLENT)
        );
    }

    @Test
    void removeLastGradeFromSubject() {
        StudentRecord record = createRecord();
        record.addSubject(1, createSubject("Физика"));
        record.addGrade(1, "Физика", Grade.SATISFACTORY);
        record.addGrade(1, "Физика", Grade.GOOD);
        record.removeLastGrade(1, "Физика");
        Subject subject = record.getSemesters().get(0).getSubjects().get(0);
        assertEquals(List.of(Grade.SATISFACTORY), subject.getHistory());
        assertEquals(Grade.SATISFACTORY, subject.getLastGrade());
    }

    @Test
    void removeLastGradeFromSubjectWithoutGrades() {
        StudentRecord record = createRecord();
        record.addSubject(1, createSubject("Физика"));
        assertThrows(
                IllegalStateException.class,
                () -> record.removeLastGrade(1, "Физика")
        );
    }

    @Test
    void removeLastGradeFromNonexistentSubject() {
        StudentRecord record = createRecord();
        assertThrows(
                IllegalArgumentException.class,
                () -> record.removeLastGrade(1, "Физика")
        );
    }

    @Test
    void removeLastGradeFromNonexistentSemester() {
        StudentRecord record = createRecord();
        assertThrows(
                IllegalArgumentException.class,
                () -> record.removeLastGrade(4, "Физика")
        );
    }

    @Test
    void addQualificationWorkGrade() {
        StudentRecord record = createRecord();
        record.addQualificationWorkGrade(Grade.EXCELLENT);
        assertEquals(Grade.EXCELLENT, record.getQualificationWorkGrade());
    }

    @Test
    void addSeveralQualificationWorkGrades() {
        StudentRecord record = createRecord();
        record.addQualificationWorkGrade(Grade.GOOD);
        record.addQualificationWorkGrade(Grade.EXCELLENT);
        assertEquals(Grade.EXCELLENT, record.getQualificationWorkGrade());
    }

    @Test
    void removeQualificationWorkGrade() {
        StudentRecord record = createRecord();
        record.addQualificationWorkGrade(Grade.EXCELLENT);
        record.removeLastQualificationWorkGrade();
        assertNull(record.getQualificationWorkGrade());
    }

    @Test
    void removeQualificationWorkGradeWhenHistoryIsEmpty() {
        StudentRecord record = createRecord();
        assertThrows(
                IllegalStateException.class,
                record::removeLastQualificationWorkGrade
        );
    }

    @Test
    void removeLastQualificationWorkGradeKeepsPreviousGrade() {
        StudentRecord record = createRecord();
        record.addQualificationWorkGrade(Grade.SATISFACTORY);
        record.addQualificationWorkGrade(Grade.EXCELLENT);
        record.removeLastQualificationWorkGrade();
        assertEquals(Grade.SATISFACTORY, record.getQualificationWorkGrade());
    }

    @Test
    void addGradeToLastSubjectInSemester() {
        StudentRecord record = createRecord();
        record.addSubject(1, createSubject("Математика"));
        record.addSubject(1, createSubject("Физика"));
        record.addSubject(1, createSubject("Программирование"));
        record.addGrade(1, "Программирование", Grade.EXCELLENT);
        List<Subject> subjects = record.getSemesters().get(0).getSubjects();
        assertEquals(Grade.EXCELLENT, subjects.get(2).getLastGrade());
    }

    @Test
    void addGradeToNonexistentSubject() {
        StudentRecord record = createRecord();
        record.addSubject(1, createSubject("Математика"));
        assertThrows(
                IllegalArgumentException.class,
                () -> record.addGrade(1, "Физика", Grade.EXCELLENT)
        );
    }

    @Test
    void removeLastSemesterKeepsCurrentSemesterNumber() {
        StudentRecord record = createRecord();
        record.setCurrentSemesterNumber(1);
        record.removeLastSemester();
        assertEquals(2, record.getSemesters().size());
        assertEquals(1, record.getCurrentSemesterNumber());
    }

    @Test
    void removeOnlySemester() {
        StudentRecord record = new StudentRecord(StudentRecord.StudyForm.BUDGET);
        record.addSemester();
        record.setCurrentSemesterNumber(1);
        record.removeLastSemester();
        assertTrue(record.getSemesters().isEmpty());
        assertEquals(0, record.getCurrentSemesterNumber());
    }

    @Test
    void removeSubjectFromOneSemesterDoesNotAffectAnother() {
        StudentRecord record = createRecord();
        record.addSubject(1, createSubject("Английский язык"));
        record.addSubject(2, createSubject("Английский язык"));
        record.removeSubject(1, "Английский язык");
        assertTrue(record.getSemesters().get(0).getSubjects().isEmpty());
        assertEquals(
                1,
                record.getSemesters().get(1).getSubjects().size()
        );
    }
}