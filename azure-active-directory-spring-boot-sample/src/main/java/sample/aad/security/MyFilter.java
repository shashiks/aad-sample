package sample.aad.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

public class MyFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpReq = (HttpServletRequest) request;
		chain.doFilter(request, response);

		
	System.out.println("request.getHeader(TOKEN_HEADER) " + httpReq.getHeader("Authorization"));
	
	try {
		System.out.println("httpReq.getSession() " + httpReq.getSession().getId());
	} catch (Exception e) {
		System.out.println("Session out" + e);
	}

	System.out.println("httpReq.getCookies() " + httpReq.getCookies());
	if(httpReq.getCookies() != null) {
		System.out.println("	httpReq.getCookies().length; " + 	httpReq.getCookies().length);
	}
	
	
	Object c = SecurityContextHolder.getContext();
	if(c!= null ) {
		
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		if(c!= null ) {
			System.out.println("Authentication  from Context  " + a);
			System.out.println("a.getAuthorities().toString()  " + a.getAuthorities().toString());
			Object principal = a.getPrincipal();
			System.out.println("princi  " + principal);
    	if (principal instanceof UserDetails) {
      	  String username = ((UserDetails)principal).getUsername();
      	} else {
      	  String username = principal.toString();
      	}
		
	}
}
logger.info("< doFilter");

//		chain.doFilter(request, response);

	}
}