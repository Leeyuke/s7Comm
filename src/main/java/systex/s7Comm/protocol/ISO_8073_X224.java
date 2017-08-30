package systex.s7Comm.protocol;

public class ISO_8073_X224 {

	private String Length = "02";
	private String PDUType = "f0";
	private String Last = "80";
	private String[] Protocol = new String[]{Length, PDUType, Last};
	
	public String[] getProtocol() {
		return Protocol;
	}
	public String getLength() {
		return Length;
	}
	public String getPDUType() {
		return PDUType;
	}
	public String getLast() {
		return Last;
	}
}
