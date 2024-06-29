// Copyright 2002, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * The PromptInputStream reads from an inputstream until it reads any prompt for
 * which a listener is added. The listener is informed that the prompt is found.
 * The route which contains the prompt is supplied as a parameter to the
 * listener. Returning from the prompt listener without reading the route to its
 * end will allow the main stream to read it. The implementation of this class
 * is based on the RoutedInputStream.
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/util/io/PromptInputStream.java
 *          96b41b903496 2005/11/21 19:50:18 duns $
 */
public class PromptInputStream extends RoutedInputStream {

	/**
	 * Craete a Prompt input stream.
	 * 
	 * @param input
	 *            stream to read
	 */
	public PromptInputStream(InputStream input) {
		super(input);
	}

	/**
	 * Add a prompt listener for given prompt.
	 * 
	 * @param prompt
	 *            prompt to listen for
	 * @param listener
	 *            listener to be informed
	 */
	public void addPromptListener(String prompt, PromptListener listener) {
		final PromptListener promptListener = listener;
		addRoute(prompt, prompt, new RouteListener() {
			public void routeFound(RoutedInputStream.Route input)
					throws IOException {
				promptListener.promptFound(input);
			}
		});
	}
}
