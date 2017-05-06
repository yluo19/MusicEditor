package Service;

public class Setting {

	private static String mode="default";//Order play(default)   random play       onecircle       listcircle        oneplay  

	public static String getMode() {
		return mode;
	}

	public static void setMode(String mode) {
		Setting.mode = mode;
	}
	
}
