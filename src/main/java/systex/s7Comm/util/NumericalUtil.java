package systex.s7Comm.util;

/**
 * 版权所有:精诚瑞宝计算机系统有限公司
 * @描述:	 数值操作类 
 * @作者: yuke
 * @时间: 2017年8月28日 下午3:01:57   
 * @版本: 1.0
 */
public class NumericalUtil {

	/**
	 * 将byte数组转换为十六进制数值的数组
	 * @param byteNumber byte数组
	 * @return <strong>hexNumber</strong> Hex数组
	 */
	public static String[] byteToHex(byte[] byteNumber) {  
		String[] hexNumber = new String[byteNumber.length];
		for (int i = 0; i < byteNumber.length; i++) { 
			String hex = Integer.toHexString(byteNumber[i] & 0xFF); 
			if (hex.length() == 1) { 
				hex = '0' + hex; 
			} 
			hexNumber[i] = hex.toUpperCase();
		} 
		return hexNumber;
	}
	
	/**
	 * 将十六进制字符串数组转换为byte数组
	 * @param hexNumber 十六进制字符串数组
	 * @return byte数组
	 */
	public static byte[] hexToByte(String[] hexNumber) {  
		byte[] byteNumber = new byte[hexNumber.length];	//保存所有的byte
		for(int hexL=0; hexL<hexNumber.length; hexL++) {
		    String hexString = hexNumber[hexL].toUpperCase();  
		    int length = hexString.length() / 2;  
		    char[] hexChars = hexString.toCharArray();  
		    byte[] b = new byte[length];  
		    for (int i = 0; i < length; i++) {  
		        int pos = i * 2;  
		        b[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
		    }  
		    byteNumber[hexL] = b[0];
		}
	    return byteNumber;  
	}  
	
	 private static byte charToByte(char c) {  
		    return (byte) "0123456789ABCDEF".indexOf(c);  
		}
}
