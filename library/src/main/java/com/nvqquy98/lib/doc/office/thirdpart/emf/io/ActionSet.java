// Copyright 2001, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.io;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to keep registered Actions, which should be used by the
 * TaggedIn/OutputStream. A set of recognized Actions can be added to this
 * class. A concrete implementation of this stream should install all allowed
 * actions.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: src/main/java/org/freehep/util/io/ActionSet.java 96b41b903496
 *          2005/11/21 19:50:18 duns $
 */
public class ActionSet {

	/**
	 * This holds the individual actions.
	 */
	protected Map actions;

	protected Action defaultAction;

	/**
	 * Creates an set for actions
	 */
	public ActionSet() {
		actions = new HashMap();
		defaultAction = new Action.Unknown();
	}

	/**
	 * Adds an action to the set
	 * 
	 * @param action
	 *            to be added
	 */
	public void addAction(Action action) {
		actions.put(new Integer(action.getCode()), action);
	}

	/**
	 * Looks up the corresponding action for an action code.
	 * 
	 * @param actionCode
	 *            code to be looked for
	 * @return corresponding action, or Action.Unknown in case Action is not
	 *         found.
	 */
	public Action get(int actionCode) {
		Action action = (Action) actions.get(new Integer(actionCode));
		if (action == null) {
			action = defaultAction;
		}
		return action;
	}

	/**
	 * Looks if an Action for code is in this set.
	 * 
	 * @param actionCode
	 *            code to be looked for
	 * @return true if action exists for code
	 */
	public boolean exists(int actionCode) {
		return (actions.get(new Integer(actionCode)) != null);
	}
}
