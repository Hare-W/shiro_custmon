package security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JwtUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "customFilter",urlPatterns = "*.jsp")
public class customFilter implements Filter {

    final static Logger logger = LoggerFactory.getLogger(customFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("customFilter init()");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("customFilter doFilter()");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String url = request.getRequestURI();
        int index = url.lastIndexOf("/");
        String jspName = url.substring(index + 1);
        if(jspName.equals("login.jsp")){
            logger.info("登录页面，不拦截");
            filterChain.doFilter(servletRequest,servletResponse);
        }else if(jspName.equals("index.jsp")){
            logger.info("主页，拦截请求，判断token");
            if(isLogin(request)){
                logger.info("已经登录，继续访问index");
                //response.sendRedirect("index.jsp");
            }else {
                logger.info("未登录，重新定向到login");
                response.sendRedirect("login.jsp");
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    boolean isLogin(HttpServletRequest request){
//        String token = request.getHeader("token");
//        if(JwtUtil.verify(token)){
//            logger.info("token verify success, token =" + request.getHeader("token"));
            //JwtUtil.getUsername(token);
            return false;
//        }
//        return false;
    }

    @Override
    public void destroy() {
        logger.info("customFilter destroy()");
    }
}
