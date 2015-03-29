package by.heap.service.filter;

import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TraceFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("Request url '{}', body '{}'.", ((HttpServletRequest) request).getRequestURI(), CharStreams.toString(request.getReader()));
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
