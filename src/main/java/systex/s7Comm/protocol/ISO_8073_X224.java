package systex.s7Comm.protocol;

public class ISO_8073_X224 {

	private String[] Length = new String[]{"02"};
	private String[] PDUType = new String[]{"f0"};
	private String[] Last = new String[]{"80"};
	
	public String[] getLength() {
		return Length;
	}
	public String[] getPDUType() {
		return PDUType;
	}
	public String[] getLast() {
		return Last;
	}
}
