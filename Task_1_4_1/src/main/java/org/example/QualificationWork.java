package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Представляет квалификационную работу студента.
 * Хранит историю оценок за её выполнение и защиту.
 */
public class QualificationWork {
    private final List<Grade> history;

    /**
     * Создаёт квалификационную работу с пустой историей оценок.
     */
    public QualificationWork() {
        this.history = new ArrayList<>();
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
     * Добавляет оценку в конец истории.
     *
     * @param grade добавляемая оценка
     */
    public void addGrade(Grade grade) {
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
}