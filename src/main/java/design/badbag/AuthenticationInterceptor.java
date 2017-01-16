package design.badbag;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import design.badbag.controllers.AbstractController;
import design.badbag.models.SiteUser;
import design.badbag.models.dao.SiteUserDao;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    SiteUserDao siteUserDao;
 
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
    		Object handler, ModelAndView modelAndView) throws Exception {
	    
    	boolean isLoggedIn = false;
    	boolean editable = false;
		SiteUser siteUser;
	    Integer userId = (Integer) request.getSession().getAttribute(AbstractController.userSessionKey);
	    Integer cartSize = 0;
	
	    if (userId != null) {
        	siteUser = siteUserDao.findByUid(userId);
        	
        	if (siteUser != null) {
        		isLoggedIn = true;
        		
        		if (siteUser.getCart() != null) {
        			cartSize = siteUser.getCart().size();
        			
        		}
        	}
        }
	    
	    if (modelAndView != null) {
	    	//without if statement these cause errors on /addItem page... Waddup wit that?
			modelAndView.getModelMap().addAttribute("cartSize", cartSize);
		    modelAndView.getModelMap().addAttribute("isLoggedIn", isLoggedIn);
		}
	    
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        List<String> authPages = Arrays.asList(
        		"/addItem", 
        		"/manageItems", 
        		"/manageOrders", 
        		"/viewCart", 
        		"/checkOut"
        		);

        // Require sign-in for auth pages
        if (authPages.contains(request.getRequestURI()) ) {

        	boolean isLoggedIn = false;
        	SiteUser siteUser;
            Integer userId = (Integer) request.getSession().getAttribute(AbstractController.userSessionKey);

            if (userId != null) {
            	siteUser = siteUserDao.findByUid(userId);
            	
            	if (siteUser != null) {
            		isLoggedIn = true;
            	}
            }

            // If user not logged in, redirect to login page
            if (!isLoggedIn) {
                response.sendRedirect("/login");
                return false;
            }
        }

        return true;
    }

}