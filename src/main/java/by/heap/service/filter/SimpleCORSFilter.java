package by.heap.service.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * For developing UI.
 *
 * @author Ibragimov Ruslan
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter {

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, authorization");
        response.setHeader("Access-Control-Max-Age", "3600");

        if ("OPTIONS".equals(request.getMethod())) {
            // Do nothing.
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}