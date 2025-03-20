package edu.ustb.dubbo.service.impl;

import edu.ustb.client.api.ApiProtocol;
import edu.ustb.client.api.ApiService;
import edu.ustb.dubbo.service.DubboRPCService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author: ljr-YingWu
 * @date: 2023/11/19 16:11
 * 



 * DubboServiceImpl类
 */

@ApiService(serviceId = "backend-dubbo-server", protocol = ApiProtocol.DUBBO,
        patternPath = "/**")
@DubboService
public class DubboRPCServiceImpl implements DubboRPCService {
    @Override
    public String testDubboRPC(String msg) {
        return "hello dubbo!!!"+ msg;
    }
}
