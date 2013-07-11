package us.sustainify.web.filter;

import com.google.inject.Singleton;
import us.sustainify.common.domain.service.system.SystemSetupService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class SetupFilter implements Filter {

    private SystemSetupService systemSetupService;

    @Inject
    public SetupFilter(SystemSetupService systemSetupService) {
        this.systemSetupService = systemSetupService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(systemSetupService.isSetupRequired()) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendRedirect("/");
        }
    }

    @Override
    public void destroy() {
    }
}
