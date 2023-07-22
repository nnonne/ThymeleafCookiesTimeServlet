import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import org.thymeleaf.context.Context;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init()  {
        engine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("./templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    private static String getTimezone(HttpServletRequest req) {
        String cookies = req.getHeader("Cookie");
        Map<String, String> result = new HashMap<>();
        if (cookies != null) {
            for (String cookie : cookies.split(";")) {
                result.put(cookie.split("=")[0], cookie.split("=")[1]);
            }
        }
        return result.getOrDefault("previousTimezone", "UTC");
    }

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String currentTime = getCurrentTime(req, resp);
    Context simpleContext = new Context(req.getLocale(), Map.of("time", currentTime));
    engine.process("time", simpleContext, resp.getWriter());
    resp.getWriter().close();
}

    private static String getCurrentTime(HttpServletRequest req, HttpServletResponse resp) {
        String timezone = req.getParameter("timezone");
        if (timezone == null) {
            timezone = getTimezone(req);
        } else {
            timezone = timezone.replace(' ', '+');
            resp.addCookie(new Cookie("previousTimezone", timezone));
        }
        return ZonedDateTime
                .now(ZoneId.of(timezone))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ")) + timezone;
    }
}