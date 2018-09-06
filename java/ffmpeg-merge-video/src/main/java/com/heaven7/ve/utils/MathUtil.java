package com.heaven7.ve.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MathUtil {

	public static int log2n(int n) {
		return (int) (Math.log(n) / Math.log(2));
	}

	/***
	 * get the max 2^k . that make 2^k <= n
	 * 
	 * @param n
	 *            the target
	 * @return the max value(2^k) . which is the max value below n with value <=
	 *         n.
	 */
	public static int max2K(int n) {
		return (int) Math.pow(2, log2n(n));
	}

	/**
	 * parse the flags to list of flags
	 * @param flags the flags
	 * @return the list flags.
	 */
	public static List<Integer> parseFlags(final int flags){
		if(flags <=0 ){
			return Collections.emptyList();
		}
		List<Integer> result = new ArrayList<>();
		int curFlags = flags;
		for (; curFlags > 0;) {
			int maxKey = max2K(curFlags);
			result.add(maxKey);
			if (maxKey > 0) {
				curFlags -= maxKey;
			}
		}
		return result;
	}

}