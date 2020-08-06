/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.steps.prob101.gate_filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import java.util.Enumeration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

/**
 *
 * @author stepin
 */
@Component
public class OAuth2Filter extends ZuulFilter {

    private static final String AUTHORIZATION_HEADER = "authorization";

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

        if (ctx.getRequest().getHeader(AUTHORIZATION_HEADER) == null) {
            ctx.addZuulRequestHeader(AUTHORIZATION_HEADER, this.getTokenHeader());
        }

        return null;
    }

    private String getTokenHeader() {
        System.out.println("## FROM SecurityContextHolder:" + SecurityContextHolder.getContext());

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        System.out.println("### authentication:" + authentication);

        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        System.out.println("### authenticationToken:" + authenticationToken);
        
        String strToken = (String) authenticationToken.getPrincipal().getAttributes().get("jti");
        
        return buildBearerToken(strToken);
  }
    private String buildBearerToken(String strToken) {
        return "Bearer " + strToken;
    }

}
