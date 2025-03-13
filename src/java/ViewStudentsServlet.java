import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ViewStudentsServlet extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Connect to database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/midterm?useSSL=false", "root", "root");

            // Query to fetch students
            String query = "SELECT enrollment_no, name, course, semester, email, class FROM students";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            // Create a table to display the results
            out.println("<table border='1' style='width:100%; border-collapse:collapse;'>");
            out.println("<tr>");
            out.println("<th>Enrollment No</th>");
            out.println("<th>Name</th>");
            out.println("<th>Course</th>");
            out.println("<th>Semester</th>");
            out.println("<th>Email</th>");
            out.println("<th>Class</th>");
            out.println("<th>Actions</th>");
            out.println("</tr>");

            // Loop through result set and display data
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("enrollment_no") + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getString("course") + "</td>");
                out.println("<td>" + rs.getInt("semester") + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("<td>" + rs.getString("class") + "</td>");
                out.println("<td>");
                out.println("<button onclick=\"editStudent('" + rs.getString("enrollment_no") + "')\">Edit</button>");
                out.println("<button onclick=\"deleteStudent('" + rs.getString("enrollment_no") + "')\">Delete</button>");
                out.println("</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
        }
    }
} 