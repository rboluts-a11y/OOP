package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Представляет учебную дисциплину в зачётной книжке студента.
 * Хранит название дисциплины, тип аттестации и историю оценок.
 */
public class Subject {

    private final String name;
    private final AssessmentType assessmentType;
    private final List<Grade> history;

    /**
     * Создаёт дисциплину с пустой историей оценок.
     *
     * @param name название дисциплины
     * @param assessmentType тип аттестации
     */
    public Subject(String name, AssessmentType assessmentType) {
        this.name = name;
        this.assessmentType = assessmentType;
        this.history = new ArrayList<>();
    }

    /**
     * Возвращает название дисциплины.
     *
     * @return название дисциплины
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает тип аттестации по дисциплине.
     *
     * @return тип аттестации
     */
    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    /**
     * Возвращает копию истории оценок.
     *
     * @return неизменяемый список оценок
     */
    public List<Grade> getHistory() {
        return List.copyOf(history);
    }

    /**
     * Добавляет оценку в конец истории, если она допустима для типа аттестации дисциплины.
     *
     * @param grade добавляемая оценка
     * @throws IllegalArgumentException если оценка не соответствует типу аттестации
     */
    public void addGrade(Grade grade) {
        if (assessmentType == AssessmentType.CREDIT) {
            if (grade != Grade.PASS && grade != Grade.FAIL) {
                throw new IllegalArgumentException(
                        "Для обычного зачёта допустимы только PASS и FAIL"
                );
            }
        } else if (grade == Grade.PASS) {
            throw new IllegalArgumentException(
                    "Для экзамена и дифференцированного зачёта оценка PASS недопустима"
            );
        }

        history.add(grade);
    }

    /**
     * Возвращает последнюю полученную оценку.
     *
     * @return последняя оценка или null, если история пуста
     */
    public Grade getLastGrade() {
        if (history.isEmpty()) {
            return null;
        }
        return history.get(history.size() - 1);
    }

    /**
     * Удаляет последнюю оценку из истории.
     *
     * @throws IllegalStateException если история оценок пуста
     */
    public void removeLastGrade() {
        if (history.isEmpty()) {
            throw new IllegalStateException("Нет оценок для удаления");
        }
        history.remove(history.size() - 1);
    }

    /**
     * Тип аттестации по учебной дисциплине.
     */
    public enum AssessmentType {
        EXAM,
        DIFFERENTIATED_CREDIT,
        CREDIT
    }
}