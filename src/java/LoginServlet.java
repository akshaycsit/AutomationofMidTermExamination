import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/midterm?useSSL=false", "root", "root");

            // Use a PreparedStatement for better security
            String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, role);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Redirect based on role
                if ("student".equalsIgnoreCase(role)) {
                    response.sendRedirect("dashboardStudent.html");
                } else if ("teacher".equalsIgnoreCase(role)) {
                    response.sendRedirect("Teacher.html");
                } else if ("admin".equalsIgnoreCase(role)) {
                    response.sendRedirect("dashboardAdmin.html");
                }
            } else {
                // Display error message on the same page
                out.println("<html>");
                out.println("<body>");
                out.println("<script>alert('Invalid Username, Password, or Role. Please try again.');</script>");
                out.println("<meta http-equiv='refresh' content='0;URL=login.html'>");
                out.println("</body>");
                out.println("</html>");
            }

            // Close resources
            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            // Log the error and show a generic message
            e.printStackTrace();
            out.println("<html>");
            out.println("<body>");
            out.println("<h3 style='color:red;'>An unexpected error occurred. Please try again later.</h3>");
            out.println("<a href='login.html'>Go back to login</a>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }
}
