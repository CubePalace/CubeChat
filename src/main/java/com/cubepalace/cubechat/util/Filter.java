package com.cubepalace.cubechat.util;

public class Filter {

	public static String convert(String input) {
		String msg = input;
		msg = msg.replace("4", "a");
		msg = msg.replace("@", "a");
		msg = msg.replace("<", "c");
		msg = msg.replace("3", "e");
		msg = msg.replace("1", "i");
		msg = msg.replace("!", "i");
		msg = msg.replace("|", "i");
		msg = msg.replace("$", "s");
		msg = msg.replace("5", "s");
		msg = msg.replace("9", "g");
		msg = msg.replace(".", "");
		msg = msg.replace(",", "");
		msg = msg.replace(" ", "");
		msg = msg.replace("/", "");
		msg = msg.replace("-", "");
		return msg;
	}

}
