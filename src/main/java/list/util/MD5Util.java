package list.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 使用DES加密和解密的方法
 */
public class MD5Util
{
	private static Logger logger = LoggerFactory.getLogger(MD5Util.class);

	private MD5Util(){}
	
	/**
	 * MD5加密
	 * @param pwd
	 * @return
	 */
	public static String getMD5(String pwd) 
    {
		try {
		    MessageDigest md = MessageDigest.getInstance("MD5");
		    md.update(pwd.getBytes());
		    byte[] b = md.digest();
		    int i;
		    StringBuilder buf = new StringBuilder("");
		    for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
			    i += 256;
			if (i < 16)
			    buf.append("0");
			buf.append(Integer.toHexString(i));
		    }
		    return buf.toString();
		} catch (NoSuchAlgorithmException e) {
		    logger.error("NoSuchAlgorithmException", e);
		    return null;
		}
    }

}