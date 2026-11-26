package org.example;

/**
 * Главный класс приложения.
 * Демонстрирует пример зачетной книжки.
 */
public class Main {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out,
                true, java.nio.charset.StandardCharsets.UTF_8));
        StudentRecord record = new StudentRecord(StudentRecord.StudyForm.PAID);
        record.addSemester();
        record.addSubject(1, new Subject("Математический анализ",
                Subject.AssessmentType.EXAM));
        record.addSubject(1, new Subject("Программирование",
                Subject.AssessmentType.EXAM));
        record.addSubject(1, new Subject("История",
                Subject.AssessmentType.CREDIT));
        record.addGrade(1, "Математический анализ", Grade.EXCELLENT);
        record.addGrade(1, "Программирование", Grade.GOOD);
        record.addGrade(1, "История", Grade.PASS);
        record.addSemester();
        record.addSubject(2, new Subject("Математический анализ",
                Subject.AssessmentType.EXAM));
        record.addSubject(2, new Subject("Дискретная математика",
                Subject.AssessmentType.DIFFERENTIATED_CREDIT));
        record.addSubject(2, new Subject("Физика",
                Subject.AssessmentType.CREDIT));
        record.addGrade(2, "Математический анализ", Grade.EXCELLENT);
        record.addGrade(2, "Дискретная математика", Grade.EXCELLENT);
        record.addGrade(2, "Физика", Grade.PASS);
        record.setCurrentSemesterNumber(2);
        System.out.println("========================================");
        System.out.println("       ЭЛЕКТРОННАЯ ЗАЧЁТНАЯ КНИЖКА");
        System.out.println("========================================");
        System.out.println("Форма обучения: " + record.getStudyForm());
        System.out.println("Текущий семестр: " + record.getCurrentSemesterNumber());
        System.out.printf("%nСредний балл: %.2f%n", record.getAverageScore());
        System.out.println("Перевод на бюджет: "
                + (record.canTransferToBudget() ? "возможен" : "невозможен"));
        System.out.println("Красный диплом: "
                + (record.canGetRedDiploma() ? "возможен" : "пока невозможен"));
        System.out.println("Повышенная стипендия: "
                + (record.canGetIncreasedScholarship() ? "положена" : "не положена"));
        System.out.println("========================================");
    }
}