package demo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

@Component
public class SessionSavingZuulPreFilter extends ZuulFilter {
    @Autowired
    private SessionRepository repository;

    private static final Logger log = LoggerFactory.getLogger(SessionSavingZuulPreFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();

        HttpSession httpSession = context.getRequest().getSession();
        Session session = repository.getSession(httpSession.getId());

        context.addZuulRequestHeader("Cookie", "SESSION=" + httpSession.getId());

        log.trace("ZuulPreFilter session proxy: {}", session.getId());

        return null;
    }
}