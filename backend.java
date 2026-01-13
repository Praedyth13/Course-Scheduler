import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.Calendar;

public class DBConnection {
    private static Connection connection;
    private static final String user = "java";
    private static final String password = "java";
    private static final String database = "jdbc:derby://localhost:1527/CourseSchedulerDBRobertKeyserrck5368";

    public static Connection getConnection(){
        if (connection == null){
            try{
                connection = DriverManager.getConnection(database, user, password);
            } 
            catch (SQLException e){
                e.printStackTrace();
                System.out.println("Could not open database.");
                System.exit(1);
            }
        }
        return connection;
    }

}

public class StudentQueries {
    private static Connection connection;
    private static PreparedStatement addStudent;
    private static PreparedStatement dropStudent;
    private static PreparedStatement getStudent;
    private static PreparedStatement getAllStudents;
    private static ResultSet resultSet;
    
    public static void addStudent(StudentEntry student){
        String ID = student.getStudentID();
        String first = student.getFirstName();
        String last = student.getLastName();
        connection = DBConnection.getConnection();    
        
        try{
            addStudent = connection.prepareStatement("insert into students (StudentID, FirstName, LastName) values (?,?,?)");
            addStudent.setString(1, ID);
            addStudent.setString(2, first);
            addStudent.setString(3, last);
            addStudent.executeUpdate();
        }        
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }        
    }
    
    public static void dropStudent(String ID){
        connection = DBConnection.getConnection();    
        
        try{
            dropStudent = connection.prepareStatement("delete from students where studentID = ?");
            dropStudent.setString(1, ID);
            dropStudent.executeUpdate();
        }        
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static StudentEntry getStudent(String ID){
        connection = DBConnection.getConnection();
        StudentEntry student = null;
        
        try{
            getStudent = connection.prepareStatement("select firstName, lastName from students where studentID = ?");
            getStudent.setString(1, ID);
            resultSet = getStudent.executeQuery();
            
            String first = resultSet.getString("firstName");
            String last = resultSet.getString("lastName");
            student = new StudentEntry(ID, first, last); 
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return student;
    }
    
    public static ArrayList<StudentEntry> getAllStudents(){
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> students = new ArrayList<>();
        
        try{
            getAllStudents = connection.prepareStatement("select studentID, firstName, lastName from students order by studentID");
            resultSet = getAllStudents.executeQuery();
            
            while(resultSet.next()){
                String id = resultSet.getString("studentID");
                String first = resultSet.getString("firstName");
                String last = resultSet.getString("lastName");
                StudentEntry student = new StudentEntry(id, first, last);
                students.add(student);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return students;        
    }
}

public class ClassQueries {
    private static Connection connection;
    private static PreparedStatement addClass;
    private static PreparedStatement getAllCourseCodes;
    private static PreparedStatement getClassSeats;
    private static PreparedStatement dropClass;
    private static ResultSet resultSet;
    
    public static void addClass(ClassEntry classEntry){
        String code = classEntry.getCourseCode();
        String semester = classEntry.getSemester();
        int seats = classEntry.getSeats();       
        connection = DBConnection.getConnection();  
        
        try{
            addClass = connection.prepareStatement("insert into classes (Semester, CourseCode, seats) values (?,?,?)");
            addClass.setString(1, semester);
            addClass.setString(2, code);
            addClass.setInt(3, seats);
            addClass.executeUpdate();
        }        
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }        
    }
    
    public static void dropClass(String semester, String code){
        connection = DBConnection.getConnection();
        
        try{
            dropClass = connection.prepareStatement("delete from classes where semester = ? and coursecode = ?");
            dropClass.setString(1, semester);
            dropClass.setString(2, code);
            dropClass.executeUpdate();
        }        
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        } 
    }
    
    public static ArrayList<String> getAllCourseCodes(String semester){
        connection = DBConnection.getConnection();
        ArrayList<String> courseCodes = new ArrayList<>();
        
        try{
            getAllCourseCodes = connection.prepareStatement("select courseCode from classes where semester = ? order by CourseCode");
            getAllCourseCodes.setString(1, semester);
            resultSet = getAllCourseCodes.executeQuery();
            
            while(resultSet.next()){
                courseCodes.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return courseCodes;        
    }
    
    public static int getClassSeats(String semester, String courseCode){
        connection = DBConnection.getConnection();
        int seats = 0;
        try{
            getClassSeats = connection.prepareStatement("select seats from classes where semester = ? and courseCode = ?");
            getClassSeats.setString(1, semester);
            getClassSeats.setString(2, courseCode);
            resultSet = getClassSeats.executeQuery();
            
            while(resultSet.next()){
               seats += resultSet.getInt("seats");
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return seats; 
    }    
}

public class CourseQueries {
    private static Connection connection;
    private static PreparedStatement addCourse;
    private static PreparedStatement getAllCourseCodes;
    private static ResultSet resultSet;
    
    public static void addCourse(CourseEntry course){
        String code = course.getCourseCode();
        String description = course.getCourseDescription();        
        connection = DBConnection.getConnection();        
        
        try{
            addCourse = connection.prepareStatement("insert into courses (CourseCode, Description) values (?,?)");
            addCourse.setString(1, code);
            addCourse.setString(2, description);
            addCourse.executeUpdate();
        }        
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }        
    }
    
    public static ArrayList<String> getAllCourseCodes(){
        connection = DBConnection.getConnection();
        ArrayList<String> courseCodes = new ArrayList<>();
        
        try{
            getAllCourseCodes = connection.prepareStatement("select courseCode from courses order by CourseCode");
            resultSet = getAllCourseCodes.executeQuery();
            
            while(resultSet.next()){
                courseCodes.add(resultSet.getString("courseCode"));
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return courseCodes;        
    }
}

public class SemesterQueries {
    private static Connection connection;
    private static PreparedStatement addSemester;
    private static PreparedStatement getSemesterList;
    private static ResultSet resultSet;
    
    public static void addSemester(String semester){
        connection = DBConnection.getConnection();  
        
        try{
            addSemester = connection.prepareStatement("insert into semester (semester) values (?)");
            addSemester.setString(1, semester);
            addSemester.executeUpdate();
        }        
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }        
    }
    
    public static ArrayList<String> getSemesterList(){
        connection = DBConnection.getConnection();
        ArrayList<String> semester = new ArrayList<>();
        
        try{
            getSemesterList = connection.prepareStatement("select semester from semester order by semester");
            resultSet = getSemesterList.executeQuery();
            
            while(resultSet.next()){
                semester.add(resultSet.getString("semester"));
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return semester;
        
    }
    
    
}

public class ScheduleQueries {
    private static Connection connection;
    private static PreparedStatement addSchedule;
    private static PreparedStatement dropStudentScheduleByCourse;
    private static PreparedStatement dropScheduleByCourse;
    private static PreparedStatement removeScheduleEntry;
    private static PreparedStatement replaceScheduleEntry;
    private static PreparedStatement getScheduleByStudent;
    private static PreparedStatement getScheduledStudentCount;
    private static PreparedStatement getWaitlistedStudentsByClass;
    private static ResultSet resultSet;
    
    public static void addScheduleEntry(ScheduleEntry entry){
        String semester = entry.getSemester();
        String code = entry.getCourseCode();
        String ID = entry.getStudentID();
        String status = entry.getStatus();
        Timestamp timestamp = entry.getTimestamp();        
        connection = DBConnection.getConnection(); 
        
        try{
            addSchedule = connection.prepareStatement("insert into schedule "
                                                        + "(Semester, courseCode, StudentID, status, timestamp)"
                                                        + " values (?,?,?,?,?)");
            addSchedule.setString(1, semester);
            addSchedule.setString(2, code);
            addSchedule.setString(3, ID);
            addSchedule.setString(4, status);
            addSchedule.setTimestamp(5, timestamp);
            addSchedule.executeUpdate();
        }        
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }        
    }
    
    public static void dropStudentScheduleByCourse(String semester, String code, String ID){
        connection = DBConnection.getConnection(); 
        
        try{
            dropStudentScheduleByCourse = connection.prepareStatement("delete from schedule "
                                                        + "where semester = ? and courseCode = ? and studentID = ?");
            dropStudentScheduleByCourse.setString(1, semester);
            dropStudentScheduleByCourse.setString(2, code);
            dropStudentScheduleByCourse.setString(3, ID);
            dropStudentScheduleByCourse.executeUpdate();
        }        
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static void dropScheduleByCourse(String semester, String code){
        connection = DBConnection.getConnection(); 
        
        try{
            dropScheduleByCourse = connection.prepareStatement("delete from schedule where semester = ? and courseCode = ?");
            dropScheduleByCourse.setString(1, semester);
            dropScheduleByCourse.setString(2, code);
            dropScheduleByCourse.executeUpdate();
        }        
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static void updateScheduleEntry(ScheduleEntry entry){
        String semester = entry.getSemester();
        String code = entry.getCourseCode();
        String ID = entry.getStudentID();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        connection = DBConnection.getConnection(); 
        
        try{
            removeScheduleEntry = connection.prepareStatement("delete from schedule where semester = ? and courseCode = ? and studentID = ?");
            removeScheduleEntry.setString(1, semester);
            removeScheduleEntry.setString(2, code);
            removeScheduleEntry.setString(3, ID);
            removeScheduleEntry.executeUpdate();
        }        
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        
        ScheduleEntry newEntry = new ScheduleEntry(semester, code, ID, "S", timestamp);
        addScheduleEntry(newEntry);
    }
    
    public static ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String studentID){
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> schedules = new ArrayList<>();
        
        try{
            getScheduleByStudent = connection.prepareStatement("select semester, courseCode, studentID, status, timestamp from schedule where semester = ? and studentID = ? order by courseCode");
            getScheduleByStudent.setString(1, semester);
            getScheduleByStudent.setString(2, studentID);            
            resultSet = getScheduleByStudent.executeQuery();
            
            while(resultSet.next()){
                String sem = resultSet.getString("semester");
                String courseCode = resultSet.getString("courseCode");
                String id = resultSet.getString("studentID");                
                String status = resultSet.getString("status");
                Timestamp timestamp = resultSet.getTimestamp("timestamp");
                ScheduleEntry schedule = new ScheduleEntry(sem, courseCode, id, status, timestamp);
                schedules.add(schedule);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return schedules;        
    }
    
    public static int getScheduledStudentCount(String semester, String courseCode){
        connection = DBConnection.getConnection();
        int students = 0;
        
        try{
            getScheduledStudentCount = connection.prepareStatement("select studentID from schedule where semester = ? and courseCode = ? and status = ?");
            getScheduledStudentCount.setString(1, semester);
            getScheduledStudentCount.setString(2, courseCode);
            getScheduledStudentCount.setString(3, "S");
            resultSet = getScheduledStudentCount.executeQuery();
            
            while(resultSet.next()){
                students += 1;
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return students; 
    }    
    
    public static int getWaitlistedStudentsByClass(String semester, String code){
        connection = DBConnection.getConnection();
        int students = 0;
        
        try{
            getWaitlistedStudentsByClass = connection.prepareStatement("select studentID from schedule where semester = ? and courseCode = ? and status = ?");
            getWaitlistedStudentsByClass.setString(1, semester);
            getWaitlistedStudentsByClass.setString(2, code);
            getWaitlistedStudentsByClass.setString(3, "W");
            resultSet = getScheduledStudentCount.executeQuery();
            
            while(resultSet.next()){
                students += 1;
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return students;
    }
}

public class MultiTableQueries {
    private static Connection connection;
    private static PreparedStatement getAllClasses;
    private static PreparedStatement getAllClassDescriptions;
    private static PreparedStatement getScheduledStudentsByClass;
    private static PreparedStatement getWaitlistedStudentsByClass;
    private static PreparedStatement getStudentsByID;
    private static ResultSet resultSet;
    private static ResultSet secondSet;
    
    public static ArrayList<ClassDescription> getAllClassDescriptions(String semester){
        connection = DBConnection.getConnection();
        ArrayList<ClassDescription> descriptions = new ArrayList<>();
        
        try{
            getAllClasses = connection.prepareStatement("select coursecode, seats from classes where semester = ? order by coursecode");
            getAllClasses.setString(1, semester);
            resultSet = getAllClasses.executeQuery();
            
            while(resultSet.next()){
                String code = resultSet.getString("courseCode");
                int seats = resultSet.getInt("seats");
                
                getAllClassDescriptions = connection.prepareStatement("select description from courses where coursecode = ? order by coursecode");
                getAllClassDescriptions.setString(1, code);
                secondSet = getAllClassDescriptions.executeQuery();
                
                while(secondSet.next()){
                String title = secondSet.getString("description");
                ClassDescription description = new ClassDescription(code, title, seats);
                descriptions.add(description);
                }
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return descriptions;        
    }
    
    public static ArrayList<StudentEntry> getScheduledStudentsByClass(String semester, String code){
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> students = new ArrayList<>();
        
        try{
            getScheduledStudentsByClass = connection.prepareStatement("select studentID from schedule where semester = ? and courseCode = ? and status = ? order by studentID");
            getScheduledStudentsByClass.setString(1, semester);
            getScheduledStudentsByClass.setString(2, code);
            getScheduledStudentsByClass.setString(3, "S");
            resultSet = getScheduledStudentsByClass.executeQuery();
            
            while(resultSet.next()){
                String ID = resultSet.getString("studentID");
                
                getStudentsByID = connection.prepareStatement("select firstName, lastName from students where studentID = ?");
                getStudentsByID.setString(1, ID);
                secondSet = getStudentsByID.executeQuery();
                
                while(secondSet.next()){
                String first = secondSet.getString("firstName");
                String last = secondSet.getString("lastName");
                StudentEntry student = new StudentEntry(ID, first, last);
                students.add(student);
                }
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return students;
    }
    
    public static ArrayList<StudentEntry> getWaitlistedStudentsByClass(String semester, String code){
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> students = new ArrayList<>();
        
        try{
            getWaitlistedStudentsByClass = connection.prepareStatement("select studentID from schedule where semester = ? and courseCode = ? and status = ? order by timestamp");
            getWaitlistedStudentsByClass.setString(1, semester);
            getWaitlistedStudentsByClass.setString(2, code);
            getWaitlistedStudentsByClass.setString(3, "W");
            resultSet = getWaitlistedStudentsByClass.executeQuery();
            
            while(resultSet.next()){
                String ID = resultSet.getString("studentID");
                
                getStudentsByID = connection.prepareStatement("select firstName, lastName from students where studentID = ?");
                getStudentsByID.setString(1, ID);
                secondSet = getStudentsByID.executeQuery();
                
                while(secondSet.next()){
                String first = secondSet.getString("firstName");
                String last = secondSet.getString("lastName");
                StudentEntry student = new StudentEntry(ID, first, last);
                students.add(student);
                }
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return students;
    }
}

public class StudentEntry {
    private final String studentID;
    private final String firstName;
    private final String lastName;
    
    public StudentEntry(String ID, String first, String last){
        this.studentID = ID;
        this.firstName = first;
        this.lastName = last;
    }
    
    public String getStudentID(){
        return studentID;
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
    
    @Override
    public String toString() {
        return lastName + ", " + firstName + " " + studentID;
    }
}

public class CourseEntry {
    private final String code;
    private final String description;
    
    public CourseEntry(String code, String description){
        this.code = code;
        this.description = description;
    }
    
    public String getCourseCode(){
        return code;
    }
    
    public String getCourseDescription(){
        return description;
    }
    
}

public class ClassDescription {
    private final String courseCode;
    private final String description;
    private final int seats;
    
    public ClassDescription(String code, String description, int seats){
        this.courseCode = code;
        this.description = description;
        this.seats = seats;
    } 
    
    public String getCourseCode(){
        return courseCode;
    }
    
    public String getDescription(){
        return description;
    }
    
    public int getSeats(){
        return seats;
    }
}

public class ClassEntry {
    private final String semester;
    private final String courseCode;
    private final int seats;
    
    public ClassEntry(String semester, String code, int seats){
        this.semester = semester;
        this.courseCode = code;
        this.seats = seats;
    }
    
    public String getSemester(){
        return semester;
    }
    
    public String getCourseCode(){
        return courseCode;
    }
    
    public int getSeats(){
        return seats;
    }
}

public class ScheduleEntry {
    private final String semester;
    private final String courseCode;
    private final String studentID;
    private final String status;
    private final Timestamp timestamp;
    
    
    public ScheduleEntry(String semester, String code, String ID, String status, Timestamp time){
        this.semester = semester;
        this.courseCode = code;
        this.studentID = ID;
        this.status = status;
        this.timestamp = time;
    }
    
    public String getSemester(){
        return semester;
    }
    
    public String getCourseCode(){
        return courseCode;
    }
    
    public String getStudentID(){
        return studentID;
    }
    
    public String getStatus(){
        return status;
    }
    
    public Timestamp getTimestamp(){
        return timestamp;
    }
}
