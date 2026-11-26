package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class QualificationWorkOperationsTest {
    @Test
    void createQualificationWork() {
        QualificationWork work = new QualificationWork();
        assertTrue(work.getHistory().isEmpty());
        assertNull(work.getLastGrade());
    }

    @Test
    void addGrade() {
        QualificationWork work = new QualificationWork();
        work.addGrade(Grade.GOOD);
        assertEquals(List.of(Grade.GOOD), work.getHistory());
        assertEquals(Grade.GOOD, work.getLastGrade());
    }

    @Test
    void addSeveralGrades() {
        QualificationWork work = new QualificationWork();
        work.addGrade(Grade.SATISFACTORY);
        work.addGrade(Grade.GOOD);
        work.addGrade(Grade.EXCELLENT);
        assertEquals(
                List.of(
                        Grade.SATISFACTORY,
                        Grade.GOOD,
                        Grade.EXCELLENT
                ),
                work.getHistory()
        );
        assertEquals(Grade.EXCELLENT, work.getLastGrade());
    }

    @Test
    void getLastGradeFromEmptyHistory() {
        QualificationWork work = new QualificationWork();
        assertNull(work.getLastGrade());
    }

    @Test
    void removeLastGrade() {
        QualificationWork work = new QualificationWork();
        work.addGrade(Grade.GOOD);
        work.addGrade(Grade.EXCELLENT);
        work.removeLastGrade();
        assertEquals(List.of(Grade.GOOD), work.getHistory());
        assertEquals(Grade.GOOD, work.getLastGrade());
    }

    @Test
    void removeOnlyGrade() {
        QualificationWork work = new QualificationWork();
        work.addGrade(Grade.EXCELLENT);
        work.removeLastGrade();
        assertTrue(work.getHistory().isEmpty());
        assertNull(work.getLastGrade());
    }

    @Test
    void removeLastGradeFromEmptyHistory() {
        QualificationWork work = new QualificationWork();
        assertThrows(
                IllegalStateException.class,
                work::removeLastGrade
        );
    }
}
