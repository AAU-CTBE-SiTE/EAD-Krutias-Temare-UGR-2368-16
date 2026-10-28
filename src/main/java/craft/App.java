package craft;

import jakarta.persistence.*;
import java.util.List;
import craft.Student;

public class App {
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // 1. Create Student
    public static void createStudent(Student student) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(student);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // 2. Select all students
    public static List<Student> getAllStudents() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s", Student.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 3. Select student by ID
    public static Student getStudentById(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Student.class, id);
        } finally {
            em.close();
        }
    }

    // 4. Select students by technology
    public static List<Student> getStudentsByTech(String tech) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s WHERE s.tech = :tech", Student.class);
            query.setParameter("tech", tech);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 5. Update student technology by ID
    public static int updateStudentTech(int id, String newTech) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Query query = em.createQuery("UPDATE Student s SET s.tech = :newTech WHERE s.id = :id");
            query.setParameter("newTech", newTech);
            query.setParameter("id", id);
            int updatedCount = query.executeUpdate();
            tx.commit();
            return updatedCount;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Main Method to Test Functionality
    public static void main(String[] args) {
        // Insert a test student
        Student student1 = new Student();
        student1.setName("John Snow");
        student1.setTech("Java");
        createStudent(student1);

        Student student2 = new Student();
        student2.setName("Mohammed Ali");
        student2.setTech("Python");
        createStudent(student2);

        // Fetch and print all students
        List<Student> allStudents = getAllStudents();
        System.out.println("--- All Students ---");
        allStudents.forEach(System.out::println);

        // Fetch by ID
        Student fetched = getStudentById(1);
        System.out.println("Fetched by ID 1: " + fetched);

        // Filter by Tech
        List<Student> javaStudents = getStudentsByTech("Java");
        System.out.println("Java Students count: " + javaStudents.size());

        // Clean up factory
        emf.close();
    }
}