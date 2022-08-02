package com.paymentsystem.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomeSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirect=null;
        Collection<? extends GrantedAuthority>authorities=authentication.getAuthorities();
        for(GrantedAuthority grantedAuthority:authorities){
            if(grantedAuthority.getAuthority().equals("ACCOUNTANT")){
                redirect="/accountant";
                break;
            }
            else if(grantedAuthority.getAuthority().equals("ADMIN")){
                redirect="/admin";
                break;
            }
            else if(grantedAuthority.getAuthority().equals("STUDENT")){
                redirect="/student";
                break;
            }

        }
        new DefaultRedirectStrategy().sendRedirect(request,response,redirect);
    }
}
