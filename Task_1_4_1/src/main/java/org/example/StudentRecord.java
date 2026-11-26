package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Представляет электронную зачётную книжку студента.
 * Хранит форму обучения, текущий семестр, все дисциплины по семестрам и квалификационную работу.
 */
public class StudentRecord {
    private static final int TOTAL_GRADED_SUBJECTS = 54;
    private static final double MINIMUM_EXCELLENT_GRADE_RATIO_FOR_RED_DIPLOMA = 0.75;

    private StudyForm studyForm;
    private int currentSemesterNumber;
    private final List<Semester> semesters;
    private final QualificationWork qualificationWork;

    /**
     * Создаёт зачётную книжку с указанной формой обучения.
     * Квалификационная работа создаётся автоматически.
     * Текущий семестр изначально не задан.
     *
     * @param studyForm форма обучения студента
     */
    public StudentRecord(StudyForm studyForm) {
        this.studyForm = studyForm;
        this.semesters = new ArrayList<>();
        this.qualificationWork = new QualificationWork();
        currentSemesterNumber = 0;
    }

    /**
     * Возвращает текущую форму обучения.
     *
     * @return форма обучения
     */
    public StudyForm getStudyForm() {
        return studyForm;
    }

    /**
     * Изменяет форму обучения.
     *
     * @param studyForm новая форма обучения
     */
    public void setStudyForm(StudyForm studyForm) {
        this.studyForm = studyForm;
    }

    /**
     * Возвращает номер текущего семестра.
     *
     * @return номер текущего семестра, начиная с 1; 0, если он не задан
     */
    public int getCurrentSemesterNumber() {
        return currentSemesterNumber;
    }

    /**
     * Устанавливает текущий семестр.
     *
     * @param currentSemesterNumber номер текущего семестра, начиная с 1
     * @throws IllegalArgumentException если семестр с таким номером не существует
     */
    public void setCurrentSemesterNumber(int currentSemesterNumber) {
        if (currentSemesterNumber < 1 || currentSemesterNumber > semesters.size()) {
            throw new IllegalArgumentException("Некорректный номер текущего семестра");
        }
        this.currentSemesterNumber = currentSemesterNumber;
    }

    /**
     * Возвращает копию списка семестров.
     *
     * @return неизменяемый список семестров
     */
    public List<Semester> getSemesters() {
        return List.copyOf(semesters);
    }

    /**
     * Добавляет следующий семестр.
     */
    public void addSemester() {
        semesters.add(new Semester());
    }

    /**
     * Удаляет последний семестр.
     * Если текущий семестр был удалён, номер текущего семестра корректируется.
     *
     * @throws IllegalStateException если список семестров пуст
     */
    public void removeLastSemester() {
        if (semesters.isEmpty()) {
            throw new IllegalStateException("Нет семестров для удаления");
        }
        semesters.remove(semesters.size() - 1);
        currentSemesterNumber = Math.min(currentSemesterNumber, semesters.size());
    }

    /**
     * Находит семестр по его номеру.
     *
     * @param semesterNumber номер семестра, начиная с 1
     * @return найденный семестр
     * @throws IllegalArgumentException если семестра с таким номером нет
     */
    private Semester getSemesterOrThrow(int semesterNumber) {
        if (semesterNumber < 1 || semesterNumber > semesters.size()) {
            throw new IllegalArgumentException(
                    "Семестр с номером " + semesterNumber + " не существует"
            );
        }
        return semesters.get(semesterNumber - 1);
    }

    /**
     * Проверяет, что текущий семестр задан и существует.
     *
     * @throws IllegalStateException если текущий семестр не задан или не существует
     */
    private void ensureCurrentSemesterIsValid() {
        if (currentSemesterNumber < 1 || currentSemesterNumber > semesters.size()) {
            throw new IllegalStateException("Текущий семестр не задан или не существует");
        }
    }

    /**
     * Добавляет дисциплину в указанный семестр.
     *
     * @param semesterNumber номер семестра
     * @param subject добавляемая дисциплина
     * @throws IllegalArgumentException если семестр не существует
     */
    public void addSubject(int semesterNumber, Subject subject) {
        getSemesterOrThrow(semesterNumber).addSubject(subject);
    }

    /**
     * Удаляет дисциплину из указанного семестра.
     *
     * @param semesterNumber номер семестра
     * @param subjectName название дисциплины
     * @throws IllegalArgumentException если семестр или дисциплина не существуют
     */
    public void removeSubject(int semesterNumber, String subjectName) {
        getSemesterOrThrow(semesterNumber).removeSubject(subjectName);
    }

    /**
     * Добавляет оценку по указанной дисциплине.
     *
     * @param semesterNumber номер семестра
     * @param subjectName название дисциплины
     * @param grade добавляемая оценка
     * @throws IllegalArgumentException если семестр или дисциплина не существуют
     */
    public void addGrade(int semesterNumber, String subjectName, Grade grade) {
        getSemesterOrThrow(semesterNumber).addGrade(subjectName, grade);
    }

    /**
     * Удаляет последнюю оценку по указанной дисциплине.
     *
     * @param semesterNumber номер семестра
     * @param subjectName название дисциплины
     * @throws IllegalArgumentException если семестр или дисциплина не существуют
     * @throws IllegalStateException если у дисциплины нет оценок
     */
    public void removeLastGrade(int semesterNumber, String subjectName) {
        getSemesterOrThrow(semesterNumber).removeLastGrade(subjectName);
    }

    /**
     * Возвращает последнюю оценку за квалификационную работу.
     *
     * @return последняя оценка или null, если работа ещё не оценена
     */
    public Grade getQualificationWorkGrade() {
        return qualificationWork.getLastGrade();
    }

    /**
     * Добавляет оценку за квалификационную работу.
     *
     * @param grade добавляемая оценка
     */
    public void addQualificationWorkGrade(Grade grade) {
        qualificationWork.addGrade(grade);
    }

    /**
     * Удаляет последнюю оценку за квалификационную работу.
     *
     * @throws IllegalStateException если история оценок пуста
     */
    public void removeLastQualificationWorkGrade() {
        qualificationWork.removeLastGrade();
    }

    /**
     * Вычисляет средний балл за весь период обучения.
     * Учитываются только последние оценки по экзаменам и дифференцированным зачётам.
     * Обычные зачёты в расчёт не включаются.
     *
     * @return средний балл, округлённый до двух знаков после запятой;
     *         0.0, если числовых оценок ещё нет
     */
    public double getAverageScore() {
        int numberGrades = 0;
        int sumGrades = 0;
        for (Semester currentSemester : semesters) {
            for (Subject currentSubject : currentSemester.getSubjects()) {
                if (currentSubject.getAssessmentType() != Subject.AssessmentType.CREDIT) {
                    Grade grade = currentSubject.getLastGrade();
                    if (grade != null) {
                        numberGrades++;
                        sumGrades += grade.getValue();
                    }
                }
            }
        }
        if (numberGrades == 0) {
            return 0.0;
        }
        double average = (double) sumGrades / numberGrades;
        return Math.round(average * 100.0) / 100.0;
    }

    /**
     * Проверяет возможность получения красного диплома по текущим результатам.
     * Учитываются последние оценки по экзаменам и дифференцированным зачётам,
     * а также последняя оценка за квалификационную работу.
     * Для получения красного диплома не должно быть итоговых оценок ниже 4,
     * не менее 75% числовых оценок должны быть равны 5,
     * а квалификационная работа должна быть оценена на 5.
     * Обычные зачёты не входят в процентный расчёт, но незакрытый зачёт
     * блокирует получение диплома.
     *
     * @return true, если красный диплом ещё возможен по текущим результатам;
     *         false в противном случае
     */
    public boolean canGetRedDiploma() {
        int numberGrades = 0;
        int excellentGrades = 0;
        boolean hasGradeUnderGood = false;
        for (Semester currentSemester : semesters) {
            for (Subject currentSubject : currentSemester.getSubjects()) {
                Grade grade = currentSubject.getLastGrade();
                if (grade == Grade.FAIL) {
                    hasGradeUnderGood = true;
                }
                if (currentSubject.getAssessmentType() != Subject.AssessmentType.CREDIT) {
                    if (grade != null) {
                        numberGrades++;
                        if (grade == Grade.EXCELLENT) {
                            excellentGrades++;
                        }
                        if (grade.getValue() < Grade.GOOD.getValue()) {
                            hasGradeUnderGood = true;
                        }
                    }
                }
            }
        }
        if (hasGradeUnderGood) {
            return false;
        }
        Grade qualificationWorkGrade = qualificationWork.getLastGrade();
        if (qualificationWorkGrade != null && qualificationWorkGrade != Grade.EXCELLENT) {
            return false;
        }
        int remainderGrades = TOTAL_GRADED_SUBJECTS - numberGrades;
        int maximumPossibleExcellentGrades = excellentGrades + remainderGrades;
        double maxPossibleExcellentRatio = (double) maximumPossibleExcellentGrades
                / TOTAL_GRADED_SUBJECTS;
        if (maxPossibleExcellentRatio < MINIMUM_EXCELLENT_GRADE_RATIO_FOR_RED_DIPLOMA) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет возможность получения повышенной стипендии.
     * В первом семестре учитываются оценки только за него,
     * начиная со второго — за два последних семестра.
     * Все числовые оценки должны быть не ниже 4.
     * Наличие более одной попытки по дисциплине означает пересдачу
     * и лишает права на повышенную стипендию.
     *
     * @return true, если условия получения повышенной стипендии выполнены;
     *         false в противном случае
     * @throws IllegalStateException если текущий семестр не задан или не существует
     */
    public boolean canGetIncreasedScholarship() {
        ensureCurrentSemesterIsValid();
        int firstSemester = Math.max(1, currentSemesterNumber - 1);
        for (int i = firstSemester - 1; i < currentSemesterNumber; i++) {
            Semester currentSemester = semesters.get(i);
            for (Subject currentSubject : currentSemester.getSubjects()) {
                if (currentSubject.getHistory().size() > 1) {
                    return false;
                }
                Grade grade = currentSubject.getLastGrade();
                if (grade == Grade.FAIL) {
                    return false;
                }
                if (currentSubject.getAssessmentType() != Subject.AssessmentType.CREDIT) {
                    if (grade != null && grade.getValue() < Grade.GOOD.getValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Проверяет возможность перевода с платной формы обучения
     * на бюджетную по результатам двух последних экзаменационных сессий.
     * Учитываются последние оценки: по экзаменам не должно быть троек,
     * а по всем видам аттестации не должно быть незакрытых задолженностей.
     * Перевод не рассматривается, если студент уже учится на бюджете
     * или ещё не завершил две экзаменационные сессии.
     *
     * @return true, если условия перевода на бюджет выполнены;
     *         false в противном случае
     * @throws IllegalStateException если текущий семестр не задан или не существует
     */
    public boolean canTransferToBudget() {
        ensureCurrentSemesterIsValid();
        if (studyForm != StudyForm.PAID || currentSemesterNumber < 2) {
            return false;
        }
        int firstSemester = currentSemesterNumber - 1;
        boolean failed = false;
        boolean hasSatisfactoryExamGrade = false;
        for (int i = firstSemester - 1; i < currentSemesterNumber; i++) {
            Semester currentSemester = semesters.get(i);
            for (Subject currentSubject : currentSemester.getSubjects()) {
                Grade grade = currentSubject.getLastGrade();
                if (grade == Grade.FAIL) {
                    failed = true;
                }
                if (currentSubject.getAssessmentType() == Subject.AssessmentType.EXAM
                        && grade == Grade.SATISFACTORY) {
                    hasSatisfactoryExamGrade = true;
                }
            }
        }
        if (failed) {
            return false;
        }
        if (hasSatisfactoryExamGrade) {
            return false;
        }
        return true;
    }

    /**
     * Форма обучения студента.
     */
    public enum StudyForm {
        BUDGET,
        PAID
    }
}