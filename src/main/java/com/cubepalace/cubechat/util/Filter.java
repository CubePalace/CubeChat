package com.cubepalace.cubechat.util;

import java.util.*;

public class Filter {

	private static Map<Character, List<Character>> conversions = new HashMap<>();

	static {
		addEntry('a', '4', '@');
		addEntry('c', '<');
		addEntry('e', '3');
		addEntry('i', '1', '!', '|');
		addEntry('s', '$');
		addEntry('g', '9');
	}

	public static boolean hasReplacement(char character) {
		return conversions.containsKey(character);
	}

	public static List<Character> getReplacements(char character) {
		return conversions.get(character);
	}

	private static void addEntry(Character key, Character... value) {
		conversions.put(key, Arrays.asList(value));
	}

}
