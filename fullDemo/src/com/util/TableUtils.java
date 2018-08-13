package com.util;

import java.nio.charset.Charset;
import java.util.zip.CRC32;

public class TableUtils {
	public static long getRecipeTable(String key,int tablesSize) {
		CRC32 crc32 = new CRC32();
		crc32.update(key.getBytes(Charset.forName("utf-8")));
		long l = crc32.getValue();
		long name = l % tablesSize;
		return name;
	}

}
