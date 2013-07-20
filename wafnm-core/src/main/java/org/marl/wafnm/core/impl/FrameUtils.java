package org.marl.wafnm.core.impl;

/** A few util routines.
 * 
 * @author kr1s
 *
 */
public class FrameUtils {

	/** Generate an uri with the given prefix.
	 * 
	 * <p>The prefix should correspond to a valid URI without fragment, this
	 * function then appends a unique number as fragment as: 
	 * <code><i>prefix</i>#<i>fragment</i></code>.
	 * 
	 * @param uriPrefix The requested prefix.
	 * @return The generated URI.
	 */
	public static String generateUri(String uriPrefix) 
	{
		return new StringBuffer(uriPrefix)
			.append("#")
			.append(String.valueOf(System.currentTimeMillis()))
			.toString();
	}

}
