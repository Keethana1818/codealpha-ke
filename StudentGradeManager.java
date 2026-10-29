package Internship;

import java.util.ArrayList;
import java.util.Scanner;

class Student {
    String name;
    double grade;

    Student(String name, double grade) {
        this.name = name;
        this.grade = grade;
    }
}

public class StudentGradeManager {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        System.out.println("=================================");
        System.out.println("       STUDENT GRADE MANAGER");
        System.out.println("=================================");

        System.out.print("Enter number of students: ");
        int n = scanner.nextInt();
        scanner.nextLine(); // consume newline

        // Input student details
        for (int i = 0; i < n; i++) {
            System.out.println("\nStudent " + (i + 1));

            System.out.print("Enter student name: ");
            String name = scanner.nextLine();

            double grade;

            while (true) {
                System.out.print("Enter grade (0-100): ");
                grade = scanner.nextDouble();

                if (grade >= 0 && grade <= 100) {
                    break;
                }

                System.out.println("Invalid grade! Please enter a value between 0 and 100.");
            }

            scanner.nextLine(); // consume newline

            students.add(new Student(name, grade));
        }

        // Calculate average, highest and lowest
        double total = 0;
        double highest = students.get(0).grade;
        double lowest = students.get(0).grade;

        Student highestStudent = students.get(0);
        Student lowestStudent = students.get(0);

        for (Student student : students) {
            total += student.grade;

            if (student.grade > highest) {
                highest = student.grade;
                highestStudent = student;
            }

            if (student.grade < lowest) {
                lowest = student.grade;
                lowestStudent = student;
            }
        }

        double average = total / students.size();

        // Display summary report
        System.out.println("\n=================================");
        System.out.println("         SUMMARY REPORT");
        System.out.println("=================================");

        System.out.printf("%-20s %-10s%n", "Student Name", "Grade");
        System.out.println("---------------------------------");

        for (Student student : students) {
            System.out.printf("%-20s %-10.2f%n",
                    student.name, student.grade);
        }

        System.out.println("---------------------------------");
        System.out.printf("Average Score : %.2f%n", average);
        System.out.printf("Highest Score : %.2f (%s)%n",
                highest, highestStudent.name);
        System.out.printf("Lowest Score  : %.2f (%s)%n",
                lowest, lowestStudent.name);

        System.out.println("=================================");

        scanner.close();
    }
}