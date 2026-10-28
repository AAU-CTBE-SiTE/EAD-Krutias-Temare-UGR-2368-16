
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class StudentDatabaseOperations {

    private static final String URL =
            "jdbc:mysql://localhost:3306/StudentsDB";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "25506688";

    public static void main(String[] args) {

        try (Connection connection =
                     DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            System.out.println("Connected to StudentsDB!");

            createTable(connection);
            insertSampleData(connection);
            retrieveData(connection);
            updateStudentName(connection, 1, "Abebe");
            deleteStudent(connection, 11);
            calculateAverageGrade(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection)
            throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                "id INT PRIMARY KEY, " +
                "firstname VARCHAR(100), " +
                "lastname VARCHAR(100), " +
                "grade INT)";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }

        System.out.println("Students table is ready.");
    }

    private static void insertSampleData(Connection connection)
            throws SQLException {

        String sql = "INSERT INTO students " +
                "(id, firstname, lastname, grade) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "firstname = VALUES(firstname), " +
                "lastname = VALUES(lastname), " +
                "grade = VALUES(grade)";

        String[][] students = {
                {"1", "Abebe", "Kebede", "85"},
                {"2", "Hana", "Bekele", "90"},
                {"3", "Dawit", "Tadesse", "78"},
                {"4", "Sara", "Mekonnen", "92"},
                {"5", "Yonas", "Alemu", "80"},
                {"6", "Mimi", "Tesfaye", "88"},
                {"7", "Nahom", "Haile", "75"},
                {"8", "Liya", "Getachew", "95"},
                {"9", "Samuel", "Desta", "83"},
                {"10", "Rahel", "Solomon", "87"},
                {"11", "Biruk", "Abate", "70"}
        };

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            for (String[] student : students) {
                statement.setInt(1, Integer.parseInt(student[0]));
                statement.setString(2, student[1]);
                statement.setString(3, student[2]);
                statement.setInt(4, Integer.parseInt(student[3]));
                statement.executeUpdate();
            }
        }

        System.out.println("11 student records inserted or refreshed.");
    }

    private static void retrieveData(Connection connection)
            throws SQLException {

        String sql = "SELECT * FROM students ORDER BY id LIMIT 5";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("\nFirst five students:");

            while (resultSet.next()) {
                System.out.println(
                        resultSet.getInt("id") + " | " +
                                resultSet.getString("firstname") + " | " +
                                resultSet.getString("lastname") + " | " +
                                resultSet.getInt("grade")
                );
            }
        }
    }

    private static void updateStudentName(
            Connection connection, int id, String newFirstName)
            throws SQLException {

        String sql =
                "UPDATE students SET firstname = ? WHERE id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, newFirstName);
            statement.setInt(2, id);

            int rows = statement.executeUpdate();
            System.out.println("\nUpdated student rows: " + rows);
        }
    }

    private static void deleteStudent(Connection connection, int id)
            throws SQLException {

        String sql = "DELETE FROM students WHERE id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rows = statement.executeUpdate();
            System.out.println("Deleted student rows: " + rows);
        }
    }

    private static void calculateAverageGrade(Connection connection)
            throws SQLException {

        String sql = "SELECT AVG(grade) AS average_grade FROM students";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                double average = resultSet.getDouble("average_grade");
                System.out.printf(
                        "Average grade: %.2f%n", average);
            }
        }
    }
}
