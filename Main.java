import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.Scanner;


public class Main {
    private static Connection conn;
    public static void main(String[] args) {
        // JDBC & Database credentials
        String url = "jdbc:postgresql://localhost:5432/a3test";
        String user = "test";
        String password = "admin";

        try { // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Connect to the database
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                Scanner scanner = new Scanner(System.in);
                int userInput;

                
                do{
                    printMenu();
                    userInput = scanner.nextInt();

                    if(userInput == 1){
                        getAllStudents();
                    }else if(userInput == 2){
                        //take user input
                        scanner.nextLine();
                        System.out.println("Enter first name");
                        String first_name = scanner.nextLine();
                
                        System.out.println("Enter last name");
                        String last_name = scanner.nextLine();
                        
                        System.out.println("Enter email");
                        String email = scanner.nextLine();
                
                        System.out.println("Enter date (yyyy-mm-dd)");
                        Date enrollmentDate = Date.valueOf(scanner.nextLine());

                        addStudent(first_name, last_name, email, enrollmentDate);

                    }else if(userInput == 3){
                        //take user input
                        scanner.nextLine();
                        System.out.println("Enter the student id you want to change");
                        int student_id = Integer.parseInt(scanner.nextLine());
                        
                        System.out.println("Enter new email");
                        String email = scanner.nextLine();

                        updateStudentEmail(student_id, email);

                    }else if(userInput == 4){
                        //take user input
                        scanner.nextLine();
                        System.out.println("Enter the student id you want to delete");
                        int student_id = scanner.nextInt();

                        deleteStudent(student_id);
                    }
                }while (userInput != 0);


            } else {
                System.out.println("Failed to establish connection.");
            } 
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printMenu(){
            System.out.println("Menu:");
            System.out.println("Enter 0: Exit");
            System.out.println("Enter 1: Get all students");
            System.out.println("Enter 2: Add a student");
            System.out.println("Enter 3: Update student email");
            System.out.println("Enter 4: Delete a student");
    }

    //prints each student in the Students table
    public static void getAllStudents(){
        try{
            //create query
            Statement statement = conn.createStatement();
            String SQL = "SELECT * FROM Students";
            statement.executeQuery(SQL);
            ResultSet rs = statement.getResultSet();
            
            System.out.println("\nAll records from student table...");
            while (rs.next()) {
                //retrieve information from each tuple and print its info
                int id = rs.getInt("student_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                Date date = rs.getDate("enrollment_date");
                System.out.println("Student ID: " + id + ", First Name: " + first_name + ", Last Name: " + last_name + ", Email: " + email + ", Enrollment Date: " + date);
            }
            System.out.println();
        }
        catch(SQLException e){e.printStackTrace();}
        
    }

    //adds a new student to Students (first_name, last_name, email, enrollment_date)
    public static void addStudent(String first_name, String last_name, String email, Date enrollment_date){
        String insertSQL = "INSERT INTO Students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        try{
            //set the variables for the query above
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            preparedStatement.setString(3, email);
            preparedStatement.setDate(4, enrollment_date);
            preparedStatement.executeUpdate();
            System.out.println("New student has been added\n");
        }
        catch(SQLException e){e.printStackTrace();}
    }

    //updates a student with student_id with new email: email
    public static void updateStudentEmail(int student_id, String email){
        try{
            //create update query
            Statement statement = conn.createStatement();
            String updateSQL = "UPDATE Students SET email='" + email + "' WHERE student_id=" +student_id;
            statement.executeUpdate(updateSQL);
            System.out.println("Student id: " + student_id + " has been updated\n");
        }
        catch(SQLException e){e.printStackTrace();}
    }

    //function deletes an entry in Students table with student_id
    public static void deleteStudent(int student_id){
        try{
            //create delete query
            Statement statement = conn.createStatement();
            String deleteSQL = "DELETE FROM Students WHERE student_id=" + student_id;
            statement.executeUpdate(deleteSQL);
            System.out.println("Student id: " + student_id + " has been deleted\n");
        }
        catch(SQLException e){e.printStackTrace();}

    }
}
