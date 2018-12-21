package com.cloudelements.cesdk.element.authorization;

import com.cloudelements.cesdk.element.freshdeskv2.FreshDeskV2AuthService;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
        filterName = "BasicProvisionHelper",
        urlPatterns = "/auth-validation",
        description = "This Class will help to provision any connector that need Basic Authentication"
)
public class BasicProvisionHelper implements Filter {

    private static final String AUTHENTICATION_HEADER = "Authorization";
    private static final String FAILURE_STRING = "Failed to authenticate with given credentials";


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String authCredentials = httpServletRequest.getHeader(AUTHENTICATION_HEADER);

            FreshDeskV2AuthService freshDeskV2AuthService = new FreshDeskV2AuthService();
            boolean provisionStatus = freshDeskV2AuthService.provision(authCredentials);

            if (servletResponse instanceof HttpServletResponse) {
                HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                if (provisionStatus) {
                    httpServletResponse.getOutputStream().write("Success".getBytes());
                    httpServletResponse.setStatus(200);
                } else {
                    httpServletResponse.getOutputStream().write(FAILURE_STRING.getBytes());
                    httpServletResponse.setStatus(401);
                }
            }

        }

    }

    @Override
    public void destroy() {

    }
}
