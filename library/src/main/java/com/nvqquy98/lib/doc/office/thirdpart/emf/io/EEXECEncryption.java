// Copyright 2001 freehep
package com.nvqquy98.lib.doc.office.thirdpart.emf.io;

//import java.util.Random;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Encrypts using the EEXEC form (Used by Type 1 fonts).
 * 
 * @author Simon Fischer
 * @version $Id: src/main/java/org/freehep/util/io/EEXECEncryption.java
 *          96b41b903496 2005/11/21 19:50:18 duns $
 */
public class EEXECEncryption extends OutputStream implements EEXECConstants {

	private int n, c1, c2, r;

	private OutputStream out;

	private boolean first = true;

	/**
	 * Creates an EEXECEncryption from given stream.
	 * 
	 * @param out
	 *            stream to write
	 */
	public EEXECEncryption(OutputStream out) {
		this(out, EEXEC_R, N);
	}

	/**
	 * Creates an EEXECEncryption from given stream.
	 * 
	 * @param out
	 *            stream to write
	 * @param r
	 */
	public EEXECEncryption(OutputStream out, int r) {
		this(out, r, N);
	}

	/**
	 * Creates an EEXECEncryption from given stream.
	 * 
	 * @param out
	 *            stream to write
	 * @param r
	 * @param n
	 */
	public EEXECEncryption(OutputStream out, int r, int n) {
		this.out = out;
		this.c1 = C1;
		this.c2 = C2;
		this.r = r;
		this.n = n;

	}

	private int encrypt(int plainByte) {
		int cipher = (plainByte ^ (r >>> 8)) % 256;
		r = ((cipher + r) * c1 + c2) % 65536;
		return cipher;
	}

	public void write(int b) throws IOException {
		if (first) {
			for (int i = 0; i < n; i++) {
				out.write(encrypt(0));
			}
			first = false;
		}

		out.write(encrypt(b));
	}

	public void flush() throws IOException {
		super.flush();
		out.flush();
	}

	public void close() throws IOException {
		flush();
		super.close();
		out.close();
	}

	private static class IntOutputStream extends OutputStream {
		int[] chars;

		int i;

		private IntOutputStream(int size) {
			chars = new int[size];
			i = 0;
		}

		public void write(int b) {
			chars[i++] = b;
		} // str += (char)b; }

		// public String getString() { return str; }
		private int[] getInts() {
			return chars;
		}
	}

	/**
	 * Encrypt array of characters.
	 * 
	 * @param chars
	 *            int array to encrypt
	 * @param r
	 * @param n
	 * @return encrypted array
	 * @throws IOException
	 *             if write fails (never happens)
	 */
	public static int[] encryptString(int[] chars, int r, int n)
			throws IOException {
		IntOutputStream resultStr = new IntOutputStream(chars.length + 4);
		EEXECEncryption eout = new EEXECEncryption(resultStr, r, n);
		for (int i = 0; i < chars.length; i++) {
			eout.write(chars[i]);
		}
		return resultStr.getInts();
	}
}
