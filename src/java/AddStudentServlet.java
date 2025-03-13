import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class AddStudentServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String enrollmentNo = request.getParameter("enrollmentNo");
        String name = request.getParameter("name");
        String course = request.getParameter("course");
        int semester = Integer.parseInt(request.getParameter("semester"));
        String email = request.getParameter("email");
        String studentClass = request.getParameter("class");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/midterm?useSSL=false", "root", "root");

            // Insert into `users` table to create a login account
            String userQuery = "INSERT INTO users (email, password, role, username) VALUES (?, ?, ?, ?)";
            PreparedStatement userStmt = con.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, email);
            userStmt.setString(2, "defaultPassword123");
            userStmt.setString(3, "student");
            userStmt.setString(4, name);
            userStmt.executeUpdate();

            // Retrieve the auto-generated user_id
            ResultSet rs = userStmt.getGeneratedKeys();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt(1);
            }

            // Insert into `students` table
            String studentQuery = "INSERT INTO students (enrollment_no, user_id, name, course, semester, email, class) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement studentStmt = con.prepareStatement(studentQuery);
            studentStmt.setString(1, enrollmentNo);
            studentStmt.setInt(2, userId);
            studentStmt.setString(3, name);
            studentStmt.setString(4, course);
            studentStmt.setInt(5, semester);
            studentStmt.setString(6, email);
            studentStmt.setString(7, studentClass);
            studentStmt.executeUpdate();

            // HTML response for pop-up
            out.println("<html>");
            out.println("<head><title>Student Added</title>");
            out.println("<script>");
            out.println("function showPopup() {");
            out.println("    document.getElementById('popup').style.display = 'block';");
            out.println("}");
            out.println("function closePopup() {");
            out.println("    document.getElementById('popup').style.display = 'none';");
            out.println("    window.location.href = 'dashboardAdmin.html';"); // Redirect after closing
            out.println("}");
            out.println("</script>");
            out.println("<style>");
            out.println("#popup {");
            out.println("    display: none;");
            out.println("    position: fixed;");
            out.println("    top: 50%;");
            out.println("    left: 50%;");
            out.println("    transform: translate(-50%, -50%);");
            out.println("    background: #fff;");
            out.println("    padding: 20px;");
            out.println("    border-radius: 10px;");
            out.println("    box-shadow: 0 4px 8px rgba(0,0,0,0.2);");
            out.println("}");
            out.println("#popup h2 { margin-bottom: 10px; }");
            out.println("#popup button {");
            out.println("    background: #3b8ad8;");
            out.println("    color: white;");
            out.println("    border: none;");
            out.println("    padding: 10px 20px;");
            out.println("    border-radius: 5px;");
            out.println("    cursor: pointer;");
            out.println("}");
            out.println("#popup button:hover { background: #2b6aab; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body onload='showPopup()'>");
            out.println("<div id='popup'>");
            out.println("<h2>Student Added Successfully!</h2>");
            out.println("<p>Enrollment Number: " + enrollmentNo + "</p>");
            out.println("<p>Name: " + name + "</p>");
            out.println("<p>Email: " + email + "</p>");
            out.println("<button onclick='closePopup()'>Close</button>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

            // Close resources
            rs.close();
            userStmt.close();
            studentStmt.close();
            con.close();
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        } finally {
            out.close();
        }
    }
}
