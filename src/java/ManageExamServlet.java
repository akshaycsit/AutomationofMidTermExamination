import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ManageExamServlet extends HttpServlet {

    /**
     * Handles POST requests to add a new exam.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if an error occurs in servlet processing
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetching form data
        String examName = request.getParameter("exam-name");
        String examDate = request.getParameter("exam-date");

        // Database connection variables
        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Load the database driver (for MySQL)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/midterm?useSSL=false", "root", "root");

            // Query to insert new exam details into the 'exams' table
            String query = "INSERT INTO exams (exam_name, exam_date) VALUES (?, ?)";
            ps = con.prepareStatement(query);
            ps.setString(1, examName);
            ps.setString(2, examDate);

            // Execute the query
            int result = ps.executeUpdate();

            // Pass the status via request attributes
            if (result > 0) {
                request.setAttribute("status", "success");
                request.setAttribute("message", "Exam added successfully!");
            } else {
                request.setAttribute("status", "error");
                request.setAttribute("message", "Error adding exam. Please try again.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            // Handle exceptions
            request.setAttribute("status", "error");
            request.setAttribute("message", "Error: " + e.getMessage());
        } finally {
            // Forward the request to the dashboard page for displaying the message
            RequestDispatcher dispatcher = request.getRequestDispatcher("/dashboardAdmin.html");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Handles GET requests to show the exam management form.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if an error occurs in servlet processing
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set content type for response
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Fetch the status and message from the request
        String status = (String) request.getAttribute("status");
        String message = (String) request.getAttribute("message");

        // Check if status and message are available
        if ("success".equals(status)) {
            out.println("<script type='text/javascript'>");
            out.println("alert('" + message + "');");
            out.println("</script>");
        } else if ("error".equals(status)) {
            out.println("<script type='text/javascript'>");
            out.println("alert('" + message + "');");
            out.println("</script>");
        }
        
        // Finish the rest of your HTML structure
        out.println("<h1>Welcome to the Dashboard</h1>");
        // Add other content for dashboard if needed
    }
}
