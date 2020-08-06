/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steps.dev.prob101.oauth2server;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 *
 * @author stepin
 */
@Configuration
@EnableAuthorizationServer
public class Auth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    //@Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;
    

//    @Autowired
//    private TokenStore tokenStore;
    @Autowired
    private MyTokenConverter myTokenConverter;

    public Auth2ServerConfig(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("kepler")
                .secret("123456")
                .scopes("read", "write")
                .authorities("TEST_AUTHORITY-WEBFRONT")
                .authorizedGrantTypes("authorization_code", "refresh_token", "implicit", "password")
                .redirectUris("http://localhost:6061/webdirect/callback")
                .accessTokenValiditySeconds(15)
                //.autoApprove(true)
                .autoApprove(false)
                .and()
                .withClient("euler")
                .secret("789")
                .scopes("read", "write")
                .authorities("TEST_AUTHORITY-WEBFRONT")
                .authorizedGrantTypes("authorization_code", "refresh_token", "implicit", "password")
                .redirectUris("http://localhost:9000/webgate/callback")
                .accessTokenValiditySeconds(10)
                //.autoApprove(true)
                .autoApprove(false)
                .and()
                .withClient("gallileo")
                .secret("123456")
                .scopes("read", "write")
                .authorities("TEST_AUTHORITY-WEBFRONT")
                .authorizedGrantTypes("authorization_code", "refresh_token", "implicit", "password")
                .redirectUris("http://localhost:9000/callback")
                .accessTokenValiditySeconds(60)
                .refreshTokenValiditySeconds(3600)
                //.autoApprove(true)
                .autoApprove(false)
                .and()
                .withClient("newton")
                .secret("789")
                .scopes("read")
                .authorities("TEST_AUTHORITY-WEBFRONT")
                .authorizedGrantTypes("authorization_code")
                .autoApprove(false);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints.tokenStore(tokenStore())
                .accessTokenConverter(myTokenConverter)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(myTokenConverter);
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

}
