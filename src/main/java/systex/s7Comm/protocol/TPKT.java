package systex.s7Comm.protocol;

/**
 * 版权所有:精诚瑞宝计算机系统有限公司
 * @描述: TPKT协议的相关内容 
 * @作者: yuke
 * @时间: 2017年8月28日 下午3:31:36   
 * @版本:
 */
public class TPKT {

	private String[] Version = new String[]{"03"};
	private String[] Reserved = new String[]{"00"};
	private String[] Length = new String[]{"00", "00"};
	
	/**
	 * 构造函数用于设置TPKT当中的Length段
	 * @param dataFrame
	 */
	public TPKT(String[] dataFrame) {
		if(dataFrame != null) {
			String lengthHex = Integer.toHexString(dataFrame.length);
			if(lengthHex.length() == 1) {
				Length = new String[]{"00", "0"+lengthHex};
			}
			else if(lengthHex.length() == 2) {
				Length = new String[]{"00", lengthHex};
			}
			else if(lengthHex.length() == 3) {
				String highLengthHex = lengthHex.substring(0, 1);
				String lowLengthHex = lengthHex.substring(1, 3);
				Length = new String[]{"0"+highLengthHex, lowLengthHex};
			}
			else if(lengthHex.length() == 4) {
				String highLengthHex = lengthHex.substring(0, 2);
				String lowLengthHex = lengthHex.substring(2, 4);
				Length = new String[]{highLengthHex, lowLengthHex};
			}
		}
	}	
	
	public String[] getVersion() {
		return Version;
	}

	public String[] getReserved() {
		return Reserved;
	}

	public String[] getLength() {
		return Length;
	}
}
