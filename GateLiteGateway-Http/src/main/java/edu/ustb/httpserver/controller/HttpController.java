package edu.ustb.httpserver.controller;

import edu.ustb.client.api.ApiInvoker;
import edu.ustb.client.api.ApiProperties;
import edu.ustb.client.api.ApiProtocol;
import edu.ustb.client.api.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: ljr-YingWu
 * @date: 2023/11/2 20:30
 * HttpController类
 */

@Slf4j
@RestController
@ApiService(serviceId = "backend-http-server", protocol = ApiProtocol.HTTP, patternPath = "/http-server/**")
public class HttpController {
    @Autowired
    private ApiProperties apiProperties;

    @ApiInvoker(path = "/http-server/ping")
    @GetMapping("/http-server/ping")
    public String ping() {
        log.info("{}", apiProperties);
        try {
            //Thread.sleep(10000000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //多服务启动的时候，这里改一下，就可以实现集群负载均衡的效果
        return "this is application1";
    }

    @ApiInvoker(path = "/http-server/ping2")
    @GetMapping("/http-server/ping2")
    public String ping2() {
        log.info("{}", apiProperties);
        try {
            //Thread.sleep(10000000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //多服务启动的时候，这里改一下，就可以实现集群负载均衡的效果
        return "this is ping1";
    }
}
