import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    private boolean isValid(String timezone) {
        try {
            ZoneId.of(timezone);
        } catch (DateTimeException e) {
            return false;
        }
        return true;
    }


    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        res.setContentType("text/html; charset=utf-8");

        String timezone = req.getParameter("timezone");
        if (timezone == null) {
            chain.doFilter(req, res);
        } else if (isValid(timezone.replace(' ', '+'))) {
            chain.doFilter(req, res);
        } else {
            res.getWriter().write("<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<title>TimeServlet</title>"
                    + "<style>"
                    + "body { font-family: Arial, sans-serif; font-size: 16px; color: #333; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<h1>INVALID TIMEZONE</h1>"
                    + "</body>"
                    + "</html>");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().close();
        }
    }
}
