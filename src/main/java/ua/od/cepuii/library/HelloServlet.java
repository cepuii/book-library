package ua.od.cepuii.library;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.db.HikariConnectionPool;
import ua.od.cepuii.library.model.Role;
import ua.od.cepuii.library.model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
  
  private String message;
  private ConnectionPool connectionPool = new HikariConnectionPool();
  
  @Override
  public void init() {
    message = "Hello stupid World!";
  }
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    User user = null;
    try (Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery("SELECT * FROM \"user\"");
      resultSet.next();
      user = new User(resultSet.getInt(1), resultSet.getString(2),
          resultSet.getString(3),
          resultSet.getTimestamp(4).toLocalDateTime(), resultSet.getBoolean(5),
          resultSet.getInt(6), Role.values()[resultSet.getInt("role_id")]);
      
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // Hello
    PrintWriter out = response.getWriter();
    out.println(
        "<html><head><link href=\"resources/css/style.css\" rel=\"stylesheet\"></head><body>");
    out.println("<h1>" + message + "</h1>");
    out.println(user);
    out.println("</body></html>");
  }
  
  @Override
  public void destroy() {
    throw new UnsupportedOperationException();
  }
}