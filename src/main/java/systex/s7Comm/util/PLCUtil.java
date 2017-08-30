package systex.s7Comm.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 版权所有:精诚瑞宝计算机系统有限公司
 * @描述: 对Plc通信操作的相关方法
 * @作者: yuke
 * @时间: 2017年8月28日 下午6:40:14   
 * @版本: 1.0
 */
public class PLCUtil {
	
	/**
	 * 上位机给下位机PLC发送数据
	 * @param socket socket连接对象
	 * @param receviedLength 接收的数据长度
	 * @param byteNumber 上位机发送的请求数据
	 * @return 下位机返回的结果数据
	 */
	public static byte[] sendToPLC(Socket socket, int receviedLength, byte[] byteNumber) {
		byte[] bs = null;
		OutputStream os;
		try {
			os = socket.getOutputStream();
			os.write(byteNumber);
			os.flush(); 
			InputStream is=socket.getInputStream();
			bs = new byte[receviedLength];
			is.read(bs);	//存放结果
		} catch (IOException e) {
			return bs;
		} 
		return bs;
	}
}
