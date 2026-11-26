package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Представляет семестр обучения.
 * Хранит список дисциплин и обеспечивает уникальность их названий.
 */
public class Semester {
    private final List<Subject> subjects;

    /**
     * Создаёт пустой семестр.
     */
    public Semester() {
        this.subjects = new ArrayList<>();
    }

    /**
     * Ищет дисциплину по названию.
     *
     * @param subjectName название искомой дисциплины
     * @return найденная дисциплина или null, если она отсутствует
     */
    private Subject findSubject(String subjectName) {
        for (Subject subject : subjects) {
            if (subject.getName().equals(subjectName)) {
                return subject;
            }
        }
        return null;
    }

    /**
     * Ищет дисциплину и выбрасывает исключение, если она отсутствует.
     *
     * @param subjectName название искомой дисциплины
     * @return найденная дисциплина
     * @throws IllegalArgumentException если дисциплина не найдена
     */
    private Subject findSubjectOrThrow(String subjectName) {
        Subject subject = findSubject(subjectName);
        if (subject == null) {
            throw new IllegalArgumentException("Дисциплина не найдена: " + subjectName);
        }
        return subject;
    }

    /**
     * Возвращает копию списка дисциплин.
     *
     * @return неизменяемый список дисциплин
     */
    public List<Subject> getSubjects() {
        return List.copyOf(subjects);
    }

    /**
     * Добавляет дисциплину в семестр.
     *
     * @param subject добавляемая дисциплина
     * @throws IllegalArgumentException если дисциплина с таким названием уже существует
     */
    public void addSubject(Subject subject) {
        if (findSubject(subject.getName()) != null) {
            throw new IllegalArgumentException(
                    "Дисциплина с таким названием уже существует: " + subject.getName()
            );
        }
        subjects.add(subject);
    }

    /**
     * Удаляет дисциплину по названию.
     *
     * @param subjectName название удаляемой дисциплины
     * @throws IllegalArgumentException если дисциплина не найдена
     */
    public void removeSubject(String subjectName) {
        Subject subject = findSubject(subjectName);
        if (subject == null) {
            throw new IllegalArgumentException("Дисциплина не найдена: " + subjectName);
        }
        subjects.remove(subject);
    }

    /**
     * Добавляет оценку по указанной дисциплине.
     *
     * @param subjectName название дисциплины
     * @param grade добавляемая оценка
     * @throws IllegalArgumentException если дисциплина не найдена
     */
    public void addGrade(String subjectName, Grade grade) {
        findSubjectOrThrow(subjectName).addGrade(grade);
    }

    /**
     * Удаляет последнюю оценку по указанной дисциплине.
     *
     * @param subjectName название дисциплины
     * @throws IllegalArgumentException если дисциплина не найдена
     * @throws IllegalStateException если история оценок пуста
     */
    public void removeLastGrade(String subjectName) {
        findSubjectOrThrow(subjectName).removeLastGrade();
    }
}
