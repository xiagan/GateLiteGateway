package edu.ustb.common.utils;

/**
 * @author: ljr-YingWu
 * @date: 2023/10/24 9:45
 * SystemUtil类
 */
public class SystemUtil {
    public static boolean isLinux() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("linux");
    }

}
