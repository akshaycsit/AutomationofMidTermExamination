import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ViewResultsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String enrollmentNo = request.getParameter("enrollmentNo");

        if (enrollmentNo == null || enrollmentNo.isEmpty()) {
            out.println("<h2>Please enter a valid Enrollment Number.</h2>");
            return;
        }

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/midterm?useSSL=false", "root", "root");

            String studentName = null;
            String studentClass = null;
            String semester = null;
            boolean hasResults = false;

            // Fetch the student's name, class, and semester
            String studentQuery = "SELECT name, class, semester FROM students WHERE enrollment_no = ?";
            PreparedStatement studentPs = con.prepareStatement(studentQuery);
            studentPs.setString(1, enrollmentNo);
            ResultSet studentRs = studentPs.executeQuery();
            if (studentRs.next()) {
                studentName = studentRs.getString("name");
                studentClass = studentRs.getString("class");
                semester = studentRs.getString("semester");
            }
            studentRs.close();
            studentPs.close();

            // Check if the student exists
            if (studentName == null) {
                out.println("<h2>No student found with Enrollment No: " + enrollmentNo + "</h2>");
                return;
            }

            // Inline CSS for styling
            out.println("<style>");
            out.println(".container { width: 60%; margin: auto; }");
            out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            out.println("th, td { padding: 8px; text-align: center; border: 1px solid #ddd; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println("h1 { text-align: center; margin-top: 20px; }");
            out.println("</style>");

            // Add the heading and container wrapper
            out.println("<h1>Acropolis Institute Of Technology And Research</h1>");
            out.println("<div class='container'>");

            // Student Details Table
            out.println("<table>");
            out.println("<tr><th colspan='2'>Student Details</th></tr>");
            out.println("<tr><td><strong>Name:</strong></td><td>" + studentName + "</td></tr>");
            out.println("<tr><td><strong>Enrollment No:</strong></td><td>" + enrollmentNo + "</td></tr>");
            out.println("<tr><td><strong>Class:</strong></td><td>" + studentClass + "</td></tr>");
            out.println("<tr><td><strong>Semester:</strong></td><td>" + semester + "</td></tr>");
            out.println("</table>");

            // Subject Results Table
            out.println("<table>");
            
            out.println("<tr>");
            out.println("<th>Subject</th>");
            out.println("<th>Grade</th>");
            out.println("<th>Score (%)</th>");
            out.println("</tr>");

            // Query to fetch results
            String query = "SELECT subjects.subject_name, results.grade, results.score " +
                           "FROM results " +
                           "JOIN subjects ON results.subject_id = subjects.subject_id " +
                           "WHERE results.enrollment_no = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, enrollmentNo);
            ResultSet rs = ps.executeQuery();

            // Display results in the table
            while (rs.next()) {
                hasResults = true;
                out.println("<tr>");
                out.println("<td>" + rs.getString("subject_name") + "</td>");
                out.println("<td>" + rs.getString("grade") + "</td>");
                out.println("<td>" + rs.getInt("score") + "%</td>");
                out.println("</tr>");
            }

            if (!hasResults) {
                out.println("<tr><td colspan='3'>No results found for this enrollment number.</td></tr>");
            }

            out.println("</table>");
            out.println("</div>"); // End of container

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            out.println("<p style='color:red; text-align:center;'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }
}