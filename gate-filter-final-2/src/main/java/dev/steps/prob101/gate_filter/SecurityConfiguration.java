/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.steps.prob101.gate_filter;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 *
 * @author stepin
 */
@Configuration
//@EnableOAuth2Client
@EnableWebSecurity(debug = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/callback**", "/accessDenied**", "/login**", "/error**", "/favicon.ico").permitAll()
                .antMatchers("/", "/**").hasAnyAuthority("TEST_AUTHORITY-WEBFRONT")
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/login")
                .and()
                .redirectionEndpoint()
                .baseUri("/callback")
                .and()
                .userInfoEndpoint()
                .userService(oauth2UserService());

    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {

        ClientRegistration registration = ClientRegistration.withRegistrationId("gallileo-reg")
                .clientId("gallileo")
                .clientSecret("123456")
                .authorizationUri("http://localhost:7000/oauth/oauth/authorize")
                .tokenUri("http://localhost:7000/oauth/oauth/token")
                .redirectUriTemplate("http://localhost:9000/callback")
                .scope("read")
                .clientName("gallileo-name")
                .userInfoUri("http://localhost:9000/user/info")
                //.userNameAttributeName("name")                 
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                //.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .build();

        return new InMemoryClientRegistrationRepository(Arrays.asList(registration));
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new MyUserService(tokenServices());
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("non-prod-signature");
        return converter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence cs) {
                return cs.toString();
            }

            @Override
            public boolean matches(CharSequence cs, String str) {
                return cs.toString().equals(str);
            }
        };
    }

//    @Bean
//    protected OAuth2ProtectedResourceDetails resourceDetails() {
//
//        AuthorizationCodeResourceDetails resourceDetails = new AuthorizationCodeResourceDetails();
//        resourceDetails.setId("gallileo_resource");
//        resourceDetails.setTokenName("gallileo_token");
//        resourceDetails.setClientId("gallileo");
//        resourceDetails.setClientSecret("123456");
//        resourceDetails.setUserAuthorizationUri("http://localhost:7000/oauth/oauth/authorize");
//        resourceDetails.setAccessTokenUri("http://localhost:7000/oauth/oauth/token");
//        resourceDetails.setScope(Arrays.asList("read"));
//        resourceDetails.setPreEstablishedRedirectUri("http://localhost:9000/callback");
//
//        resourceDetails.setUseCurrentUri(false);
//        resourceDetails.setClientAuthenticationScheme(AuthenticationScheme.header);
//
//        return resourceDetails;
//    }

}
