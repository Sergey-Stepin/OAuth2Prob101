/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steps.dev.prob101.oauth2server;

import java.security.Principal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author stepin
 */


@RestController
public class UserController {

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
    
}