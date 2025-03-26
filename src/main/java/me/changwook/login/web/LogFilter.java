package me.changwook.login.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String uri = httpServletRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        try{
            log.info("Request : [{}][{}]", uuid, uri);
            chain.doFilter(request, response);
        }catch(Exception e){
            throw e;
        }finally {
            log.info("Response : [{}][{}]", uri, uuid);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
