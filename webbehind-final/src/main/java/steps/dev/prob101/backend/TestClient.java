/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steps.dev.prob101.backend;

import org.springframework.cloud.openfeign.FeignClient;
import steps.dev.prob101.remote.contract.TestContract;

/**
 *
 * @author stepin
 */

//@FeignClient(name = "backend", configuration = TestClientConfiguration.class)
@FeignClient(name = "backend")
public interface TestClient extends TestContract{
    
}
