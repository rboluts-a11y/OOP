package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SubjectOperationsTest {
    @Test
    void createSubject() {
        Subject subject = new Subject("Математика", Subject.AssessmentType.EXAM);
        assertEquals("Математика", subject.getName());
        assertEquals(Subject.AssessmentType.EXAM, subject.getAssessmentType());
        assertTrue(subject.getHistory().isEmpty());
    }

    @Test
    void addGrade() {
        Subject subject = new Subject("Математика", Subject.AssessmentType.EXAM);
        subject.addGrade(Grade.GOOD);
        assertEquals(1, subject.getHistory().size());
        assertEquals(Grade.GOOD, subject.getLastGrade());
    }

    @Test
    void addSeveralGrades() {
        Subject subject = new Subject("Математика", Subject.AssessmentType.DIFFERENTIATED_CREDIT);
        subject.addGrade(Grade.SATISFACTORY);
        subject.addGrade(Grade.GOOD);
        subject.addGrade(Grade.EXCELLENT);
        assertEquals(3, subject.getHistory().size());
        assertEquals(Grade.EXCELLENT, subject.getLastGrade());
    }

    @Test
    void getLastGradeFromEmptyHistory() {
        Subject subject = new Subject("Математика", Subject.AssessmentType.EXAM);
        assertNull(subject.getLastGrade());
    }

    @Test
    void removeLastGrade() {
        Subject subject = new Subject("Математика", Subject.AssessmentType.DIFFERENTIATED_CREDIT);
        subject.addGrade(Grade.SATISFACTORY);
        subject.addGrade(Grade.EXCELLENT);
        subject.removeLastGrade();
        assertEquals(1, subject.getHistory().size());
        assertEquals(Grade.SATISFACTORY, subject.getLastGrade());
    }

    @Test
    void removeOnlyGrade() {
        Subject subject = new Subject("Математика", Subject.AssessmentType.EXAM);
        subject.addGrade(Grade.EXCELLENT);
        subject.removeLastGrade();
        assertTrue(subject.getHistory().isEmpty());
        assertNull(subject.getLastGrade());
    }

    @Test
    void removeLastGradeFromEmptyHistory() {
        Subject subject = new Subject("Математика", Subject.AssessmentType.DIFFERENTIATED_CREDIT);
        assertThrows(IllegalStateException.class, subject::removeLastGrade);
    }

    @Test
    void addPassToExam() {
        Subject subject = new Subject("Математика", Subject.AssessmentType.EXAM);
        assertThrows(
                IllegalArgumentException.class,
                () -> subject.addGrade(Grade.PASS)
        );
    }

    @Test
    void addExcellentToCredit() {
        Subject subject = new Subject("Физкультура", Subject.AssessmentType.CREDIT);
        assertThrows(
                IllegalArgumentException.class,
                () -> subject.addGrade(Grade.EXCELLENT)
        );
    }

    @Test
    void addPassToCredit() {
        Subject subject = new Subject("Физкультура", Subject.AssessmentType.CREDIT);
        subject.addGrade(Grade.PASS);
        assertEquals(Grade.PASS, subject.getLastGrade());
    }

    @Test
    void addFailToCredit() {
        Subject subject = new Subject("Физкультура", Subject.AssessmentType.CREDIT);
        subject.addGrade(Grade.FAIL);
        assertEquals(Grade.FAIL, subject.getLastGrade());
    }
}