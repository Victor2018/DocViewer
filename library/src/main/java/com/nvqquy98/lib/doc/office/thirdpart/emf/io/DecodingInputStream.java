// Copyright FreeHEP, 2009
package com.nvqquy98.lib.doc.office.thirdpart.emf.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Handles read by always throwing IOExceptions even in the middle of a read of
 * an array. IMPORTANT: inherits from InputStream rather than FilterInputStream
 * so that the correct read(byte[], int, int) method is used.
 * 
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public abstract class DecodingInputStream extends InputStream {

	/**
	 * Overridden to make sure it ALWAYS throws an IOException while a problem
	 * occurs in read().
	 */
	public int read(byte b[], int off, int len) throws IOException {
		if (b == null) {
			throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > b.length - off) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}

		int c = read();
		if (c == -1) {
			return -1;
		}
		b[off] = (byte) c;

		int i = 1;
		for (; i < len; i++) {
			c = read();
			if (c == -1) {
				break;
			}
			b[off + i] = (byte) c;
		}
		return i;
	}
}
