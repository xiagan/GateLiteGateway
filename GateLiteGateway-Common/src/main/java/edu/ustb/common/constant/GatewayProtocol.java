package edu.ustb.common.constant;

/**
 * @author: ljr-YingWu
 * @date: 2023/10/23 19:57
 * Test类
 */
public interface GatewayProtocol {
	
	String HTTP = "http";
	
	String DUBBO = "dubbo";
	
	static boolean isHttp(String protocol) {
		return HTTP.equals(protocol);
	}
	
	static boolean isDubbo(String protocol) {
		return DUBBO.equals(protocol);
	}
	
}
