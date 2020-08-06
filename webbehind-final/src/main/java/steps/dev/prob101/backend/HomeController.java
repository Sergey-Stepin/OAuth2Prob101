/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steps.dev.prob101.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author stepin
 */
@Controller
public class HomeController {

    @Autowired
    TestClient client;

    @GetMapping("testFeign")
    public String testfeign(Model model) {
        
        ResponseEntity<String> response = client.getTest();

        System.out.println("@@@ response" + response);

        model.addAttribute("responseStatus", response.getStatusCode());
        model.addAttribute("responseBody", response.getBody());
        return "home";

    }

}
