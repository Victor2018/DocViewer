// Copyright 2001-2009, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

/**
 * Special stream that can be used to read uncompressed first and compressed
 * from a certain byte.
 * 
 * @author Mark Donszelmann
 */
public class DecompressableInputStream extends DecodingInputStream {

	private boolean decompress;

	private InflaterInputStream iis;

	private InputStream in;
	
	private byte[] b = null;
	
	private int len = 0;
	
	private int i = 0;

	/**
	 * Creates a Decompressable input stream from given stream.
	 * 
	 * @param input
	 *            stream to read from.
	 */
	public DecompressableInputStream(InputStream input) {
		super();
		in = input;
		decompress = false;
		
		try {
			len = in.available();
			b = new byte[len];
			in.read(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int read() throws IOException {
		//return (decompress) ? iis.read() : in.read();
		if (i >= len) {
			return -1;
		}
		return  (int)(b[i++] & 0x000000FF);
	}

	public long skip(long n) throws IOException {
		//return (decompress) ? iis.skip(n) : in.skip(n);
		i += n;
		return n;
	}

	/**
	 * Start reading in compressed mode from the next byte.
	 * 
	 * @throws IOException
	 *             if read fails.
	 */
	public void startDecompressing() throws IOException {
		decompress = true;
		iis = new InflaterInputStream(in);
	}
}
