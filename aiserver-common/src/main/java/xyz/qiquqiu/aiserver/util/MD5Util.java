package xyz.qiquqiu.aiserver.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    
    /**
     * 对字符串进行MD5加密
     * @param originalPassword 原始密码
     * @return MD5加密后的字符串
     */
    public static String encode(String originalPassword) {
        try {
            // 创建MessageDigest对象，指定MD5算法
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            // 将原始密码转换为字节数组并计算哈希值
            byte[] digest = md.digest(originalPassword.getBytes());
            
            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // 理论上不会发生，因为MD5是Java标准库支持的算法
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
    
    /**
     * 验证原始密码是否与加密后的密码匹配
     * @param originalPassword 原始密码
     * @param encryptedPassword 加密后的密码
     * @return 如果匹配返回true，否则返回false
     */
    public static boolean match(String originalPassword, String encryptedPassword) {
        if (originalPassword == null || encryptedPassword == null) {
            return false;
        }
        String encryptedInput = encode(originalPassword);
        return encryptedInput.equals(encryptedPassword);
    }
}
