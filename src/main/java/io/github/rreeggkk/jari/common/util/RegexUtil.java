package io.github.rreeggkk.jari.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	public static String[] getMatches(String regex, String str) {
		List<String> allMatches = new ArrayList<String>();
		Matcher m = Pattern.compile(regex).matcher(str);
		while (m.find()) {
			allMatches.add(m.group());
		}

		return allMatches.toArray(new String[] {});
	}
}
