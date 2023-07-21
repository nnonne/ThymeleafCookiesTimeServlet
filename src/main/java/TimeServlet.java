import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        String timezone = req.getParameter("timezone");
        if (timezone == null) {
            timezone = "UTC";
        } else {
            timezone = timezone.replace(' ', '+');
        }

        String currentTime = ZonedDateTime
                .now(ZoneId.of(timezone))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ")) + timezone;
        resp.getWriter().write("<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<title>TimeServlet</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; font-size: 16px; color: #333; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<h1>"
                + currentTime
                + "</h1>"
                + "</body>"
                + "</html>");
        resp.getWriter().close();
    }
}