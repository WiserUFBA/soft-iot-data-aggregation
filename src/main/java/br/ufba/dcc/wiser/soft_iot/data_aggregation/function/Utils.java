package br.ufba.dcc.wiser.soft_iot.data_aggregation.function;

public final class Utils {

	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	
	public static boolean isFloat(String str) {
		try {
			Float.parseFloat(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	public static boolean isBoolean(String str) {
		if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false")) {
		    return true;
		}
		return false;
	}

}
