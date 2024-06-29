// Copyright 2001, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.io;

import java.io.IOException;

/**
 * Generic Action, to be used with the TagIn/OutputStreams. An action can have
 * an ActionCode, a length as well as parameters.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: src/main/java/org/freehep/util/io/Action.java 96b41b903496
 *          2005/11/21 19:50:18 duns $
 */
public abstract class Action {

	private int code;

	private String name;

	protected Action(int code) {
		this.code = code;

		name = getClass().getName();
		int dot = name.lastIndexOf(".");
		name = (dot >= 0) ? name.substring(dot + 1) : name;
	}

	/**
	 * Read an action from the input, with given actioncode and length
	 * 
	 * @param actionCode
	 *            decoded actionCode
	 * @param input
	 *            input to read from
	 * @param length
	 *            length to read
	 * @return action corresponding to actionCode
	 * @throws IOException
	 *             if read fails
	 */
	public abstract Action read(int actionCode, TaggedInputStream input,
			int length) throws IOException;

	/**
	 * @return actionCode
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return name of the action
	 */
	public String getName() {
		return name;
	}

	public String toString() {
		return "Action " + getName() + " (" + getCode() + ")";
	}

	/**
	 * Used for not recognized actions.
	 */
	public static class Unknown extends Action {
		private int[] data;

		/**
		 * Create a special Action for Unknown Actions, with actioncode 0.
		 */
		public Unknown() {
			super(0x00);
		}

		/**
		 * Create a special Action for Unknown Actions, with given action code.
		 * 
		 * @param actionCode
		 *            code to be used for Unknown Action.
		 */
		public Unknown(int actionCode) {
			super(actionCode);
		}

		public Action read(int actionCode, TaggedInputStream input, int length)
				throws IOException {

			Unknown action = new Unknown(actionCode);
			action.data = input.readUnsignedByte(length);
			return action;
		}


		public String toString() {
			return super.toString() + " UNKNOWN!, length " + data.length;
		}
	}

}
