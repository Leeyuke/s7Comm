import systex.s7Comm.util.NumericalUtil;

public class S7commTest {

	public static final byte[] COTPSEND = new byte[]{0x03, 0x00, 0x00, 0x16, 0x11, (byte) 0xe0, 
			0x00, 0x00, 0x00, 0x01, 0x00,(byte) 0xc1, 0x02, 0x10, 0x00, (byte) 0xc2, 0x02, 0x03, 0x01, (byte) 0xc0, 0x01, 0x0a};
	
	public static void main(String[] args) {

		byte[] b = NumericalUtil.hexToByte(new String[]{"02" , "f0", "80"});
		String[] str = NumericalUtil.byteToHex(b);
	}
	
}
