package util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 * This class help to parse values without error.
 * @author damzxyno
 *
 */
public final class Parser {
	private static final Logger logger = Logger.getLogger(Parser.class.getName());
	
	public static int tryGetHttpRequestIntParamOrDefault(HttpServletRequest request, String parameter) {
		var value = request.getParameter(parameter);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (Exception ex) {
				logger.log(Level.SEVERE, "Error parsing parameter ", ex);
			}
		}
		return 0;
	}
	
	public static String tryGetHttpRequestStringParamOrDefault (HttpServletRequest request, String parameter) {
		var value = request.getParameter(parameter);
		if (value != null && value.isBlank()) {
			return null;
		}
		return value;
	}
}
