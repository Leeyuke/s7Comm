package systex.s7Comm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import systex.s7Comm.util.PLCUtil;

/**
 * 版权所有:精诚瑞宝计算机系统有限公司
 * @描述: 进行S7Comm协议上位机和下位机的连接
 * @作者: yuke
 * @时间: 2017年8月28日 下午2:49:54   
 * @版本: 1.0
 */
public class s7CommLink {

	/**
	 * 用于上位机发送COTP报文的连接请求
	 */
	private static final byte[] COTPSEND = new byte[]{0x03, 0x00, 0x00, 0x16, 0x11, (byte) 0xe0, 
			0x00, 0x00, 0x00, 0x01, 0x00,(byte) 0xc1, 0x02, 0x10, 0x00, (byte) 0xc2, 0x02, 0x03, 0x01, (byte) 0xc0, 0x01, 0x0a};
	
	/**
	 * 用于上位机发送S7Comm报文的连接请求
	 */	
	private static final byte[] S7COMMSEND = new byte[]{0x03, 0x00, 0x00, 0x19, 0x02, (byte) 0xf0, 
			(byte) 0x80, 0x32, 0x01, 0x00, 0x00,(byte) 0xcc, (byte) 0xc1, 0x00, 0x08, 0x00, 0x00, 
			(byte) 0xf0, 0x00, 0x00, 0x01, 0x00, 0x01, 0x03, (byte) 0xc0};
	
	/**
	 * 上位机发送查询数据请求到下位机
	 */
	private byte[] S7COMMDBDATA = {0x03, 0x00, 0x00, 0x1f, 0x02, (byte) 0xf0, (byte) 0x80, 0x32, 0x01, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x0e, 0x00, 0x00, 0x04, 0x01, 
			0x12, 0x0a, 0x10, 0x02, 0x00, 0x00, 0x00, 0x00, (byte) 0x84, 0x00, 0x00, 0x00};
	
	/**
	 * error class 字段在S7Comm连接请求返回的byte数组的所在位置
	 */
	private final static int ErrorClassNumber = 18;
	/**
	 * error code 字段在S7Comm连接请求返回的byte数组的所在位置
	 */
	private final static int ErrorCodeNumber = 19;
	/**
	 * 要查询的db块
	 */
	private final static int[] DBNumber = {26, 27};
	/**
	 * 要查询的数据长度（以byte为单位）
	 */
	private final static int[] DATALength = {24, 25};
	/**
	 * 偏移地址（以bit为单位）
	 */
	private final static int[] DATAAddr = {29, 30, 31};
	/**
	 * plc返回数据中，不包含data内容的长度
	 */
	private final static int RECEIVED_WITHOUT_DATA_LENGTH = 25;
	/**
	 * Data字段返回的结果标志
	 */
	private final static int RETURN_CODE = 22;
	/**
	 * Socket对象用于连接
	 */
	private Socket socket = null;
	
	/**
	 * 默认102端口的构造函数
	 * @param plcIp plc的ip地址
	 */
	public s7CommLink(String plcIp) {
		if(socket == null) {
			socket = new Socket();
			SocketAddress address = new InetSocketAddress(plcIp, 102);
			try {
				socket.connect(address, 3000);
			} catch (UnknownHostException e) {
				socket = null;
			} catch (IOException e) {
				socket = null;
			}
		}
	}
	/**
	 * 构造函数用于实例化Socket对象
	 * @param plcIp plc的ip地址
	 * @param port plc端口
	 */
	public s7CommLink(String plcIp, int port) {
		if(socket == null) {
			socket = new Socket();
			SocketAddress address = new InetSocketAddress(plcIp, port);
			try {
				socket.connect(address, 3000);
			} catch (UnknownHostException e) {
				socket = null;
			} catch (IOException e) {
				socket = null;
			}
		}
	}

	/**
	 * 建立上位机与下位机PLC的连接
	 * @return 0表示正确,-1表示错误
	 */
	public int setLink() {
		if(socket == null) {
			return -1;
		}
		byte[] receviedCOTPLink = PLCUtil.sendToPLC(socket, 30, COTPSEND);	//进行COTP协议的通信
		if(receviedCOTPLink != null) {
			byte[] receviedS7CommLink = PLCUtil.sendToPLC(socket, 30, S7COMMSEND);	//进行S7Comm的连接通信
			byte errorClass = receviedS7CommLink[ErrorClassNumber - 1];		//得到ErrorClass字段的值
			byte errorCode = receviedS7CommLink[ErrorCodeNumber - 1];		//得到ErrorCode字段的值
			if(errorClass == 0 && errorCode == 0) {		//两个都为0说明连接成功
				return 0;
			}
			else {
				return -1;
			}
		}
		else {
			return -1;
		}
	}
	
	/**
	 * 进行数据的读取
	 * @param db db块
	 * @param count 读取的数量（byte为单位）
	 * @param addr 偏移的地址（bit为单位）
	 * @return 读取到的byte数据
	 */
	public byte[] dataRead(int db, int count, int addr) {
		//对db块超过255时候的判断
		if(db > 255) {
			int db_high = db/255;
			int db_low = db%255;
			S7COMMDBDATA[DBNumber[0] - 1] = (byte) db_high;	//赋值给db字段的高地址
			S7COMMDBDATA[DBNumber[1] - 1] = (byte) db_low;	//赋值给db字段的低地址
		}
		else {
			S7COMMDBDATA[DBNumber[1] - 1] = (byte) db;	//赋值给db字段的低地址
		}
		//对读取数量超过255时候的判断
		if(count > 255) {
			int count_high = count/255;
			int count_low = count%255;
			S7COMMDBDATA[DATALength[0] - 1] = (byte) count_high;	//赋值给count字段的高地址
			S7COMMDBDATA[DATALength[1] - 1] = (byte) count_low;		//赋值给count字段的低地址
		}
		else {
			S7COMMDBDATA[DATALength[1] - 1] = (byte) count;	//赋值给count字段的低地址
		}
		//对偏移地址超过255时候的判断
		if(addr > 255) {
			int addr_high = addr/255;
			int add_low = addr%255;
			S7COMMDBDATA[DATAAddr[1] - 1] = (byte) addr_high;	//赋值给address字段的中地址
			S7COMMDBDATA[DATAAddr[2] - 1] = (byte) add_low;		//赋值给address字段的低地址
		}
		else {
			S7COMMDBDATA[DATAAddr[2] - 1] = (byte) addr;	//赋值给address字段的低地址
		}
		if(socket == null) {
			return null;
		}
		byte[] receviedCOTPLink = PLCUtil.sendToPLC(socket, RECEIVED_WITHOUT_DATA_LENGTH + count, S7COMMDBDATA);
		
		byte errorClass = receviedCOTPLink[ErrorClassNumber - 1];		//得到ErrorClass字段的值
		byte errorCode = receviedCOTPLink[ErrorCodeNumber - 1];		//得到ErrorCode字段的值
		if(errorClass == 0 && errorCode == 0) {		//两个都为0说明连接成功
			byte returnCode = receviedCOTPLink[RETURN_CODE - 1];	//得到ReturnCode字段的值
			if(returnCode == (byte)0xff) {	//ff说明成功
				byte[] dataByte = new byte[count];	//存放Data数据段的结果
				for(int i=0; i<dataByte.length; i++) {
					dataByte[i] = receviedCOTPLink[RECEIVED_WITHOUT_DATA_LENGTH + i];
				}
				return dataByte;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
}
