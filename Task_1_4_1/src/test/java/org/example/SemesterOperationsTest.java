package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SemesterOperationsTest {
    @Test
    void createEmptySemester() {
        Semester semester = new Semester();
        assertTrue(semester.getSubjects().isEmpty());
    }

    @Test
    void addSubject() {
        Semester semester = new Semester();
        Subject subject = new Subject("Математика", Subject.AssessmentType.EXAM);
        semester.addSubject(subject);
        assertEquals(1, semester.getSubjects().size());
        assertSame(subject, semester.getSubjects().get(0));
    }

    @Test
    void addSeveralSubjects() {
        Semester semester = new Semester();
        Subject mathematics = new Subject("Математика", Subject.AssessmentType.EXAM);
        Subject physics = new Subject("Физика", Subject.AssessmentType.DIFFERENTIATED_CREDIT);
        Subject programming = new Subject("Программирование", Subject.AssessmentType.CREDIT);
        semester.addSubject(mathematics);
        semester.addSubject(physics);
        semester.addSubject(programming);
        assertEquals(3, semester.getSubjects().size());
        assertSame(mathematics, semester.getSubjects().get(0));
        assertSame(physics, semester.getSubjects().get(1));
        assertSame(programming, semester.getSubjects().get(2));
    }

    @Test
    void addDuplicateSubject() {
        Semester semester = new Semester();
        semester.addSubject(new Subject("Математика", Subject.AssessmentType.EXAM));
        assertThrows(
                IllegalArgumentException.class,
                () -> semester.addSubject(
                        new Subject("Математика", Subject.AssessmentType.DIFFERENTIATED_CREDIT)
                )
        );
        assertEquals(1, semester.getSubjects().size());
    }

    @Test
    void removeSubject() {
        Semester semester = new Semester();
        semester.addSubject(new Subject("Математика", Subject.AssessmentType.EXAM));
        semester.addSubject(new Subject("Физика", Subject.AssessmentType.EXAM));
        semester.removeSubject("Математика");
        assertEquals(1, semester.getSubjects().size());
        assertEquals("Физика", semester.getSubjects().get(0).getName());
    }

    @Test
    void removeLastSubject() {
        Semester semester = new Semester();
        semester.addSubject(new Subject("Математика", Subject.AssessmentType.EXAM));
        semester.removeSubject("Математика");
        assertTrue(semester.getSubjects().isEmpty());
    }

    @Test
    void removeNonexistentSubject() {
        Semester semester = new Semester();
        semester.addSubject(new Subject("Математика", Subject.AssessmentType.EXAM));
        assertThrows(
                IllegalArgumentException.class,
                () -> semester.removeSubject("Физика")
        );
        assertEquals(1, semester.getSubjects().size());
    }

    @Test
    void addGradeToSubject() {
        Semester semester = new Semester();
        Subject subject = new Subject("Математика", Subject.AssessmentType.EXAM);
        semester.addSubject(subject);
        semester.addGrade("Математика", Grade.EXCELLENT);
        assertEquals(Grade.EXCELLENT, subject.getLastGrade());
    }

    @Test
    void addSeveralGradesToSubject() {
        Semester semester = new Semester();
        Subject subject = new Subject("Математика", Subject.AssessmentType.EXAM);
        semester.addSubject(subject);
        semester.addGrade("Математика", Grade.SATISFACTORY);
        semester.addGrade("Математика", Grade.GOOD);
        semester.addGrade("Математика", Grade.EXCELLENT);
        assertEquals(3, subject.getHistory().size());
        assertEquals(Grade.EXCELLENT, subject.getLastGrade());
    }

    @Test
    void addGradeToLastSubject() {
        Semester semester = new Semester();
        semester.addSubject(new Subject("Математика", Subject.AssessmentType.EXAM));
        semester.addSubject(new Subject("Физика", Subject.AssessmentType.CREDIT));
        Subject programming = new Subject("Программирование", Subject.AssessmentType.EXAM);
        semester.addSubject(programming);
        semester.addGrade("Программирование", Grade.GOOD);
        assertEquals(Grade.GOOD, programming.getLastGrade());
    }

    @Test
    void addGradeToNonexistentSubject() {
        Semester semester = new Semester();
        semester.addSubject(new Subject("Математика", Subject.AssessmentType.EXAM));
        assertThrows(
                IllegalArgumentException.class,
                () -> semester.addGrade("Физика", Grade.EXCELLENT)
        );
    }

    @Test
    void removeLastGradeFromSubject() {
        Semester semester = new Semester();
        Subject subject = new Subject("Математика", Subject.AssessmentType.EXAM);
        semester.addSubject(subject);
        semester.addGrade("Математика", Grade.GOOD);
        semester.addGrade("Математика", Grade.EXCELLENT);
        semester.removeLastGrade("Математика");
        assertEquals(1, subject.getHistory().size());
        assertEquals(Grade.GOOD, subject.getLastGrade());
    }

    @Test
    void removeOnlyGradeFromSubject() {
        Semester semester = new Semester();
        Subject subject = new Subject("Математика", Subject.AssessmentType.EXAM);
        semester.addSubject(subject);
        semester.addGrade("Математика", Grade.EXCELLENT);
        semester.removeLastGrade("Математика");
        assertTrue(subject.getHistory().isEmpty());
        assertNull(subject.getLastGrade());
    }

    @Test
    void removeLastGradeFromSubjectWithoutGrades() {
        Semester semester = new Semester();
        semester.addSubject(new Subject("Математика", Subject.AssessmentType.EXAM));
        assertThrows(
                IllegalStateException.class,
                () -> semester.removeLastGrade("Математика")
        );
    }

    @Test
    void removeLastGradeFromNonexistentSubject() {
        Semester semester = new Semester();
        semester.addSubject(new Subject("Математика", Subject.AssessmentType.DIFFERENTIATED_CREDIT));
        assertThrows(
                IllegalArgumentException.class,
                () -> semester.removeLastGrade("Физика")
        );
    }
}
