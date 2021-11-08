package com.ontimize.boot.core.asynctask;

import com.ontimize.jee.common.exceptions.OntimizeJEEException;

public class AsyncTaskException extends OntimizeJEEException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new async task exception.
	 *
	 * @param reason
	 *            the reason
	 */
	public AsyncTaskException(String reason) {
		super(reason);
	}

	/**
	 * Instantiates a new async task exception.
	 */
	public AsyncTaskException() {
		super();
	}

	/**
	 * Instantiates a new async task exception.
	 *
	 * @param string
	 *            the string
	 * @param parent
	 *            the parent
	 */
	public AsyncTaskException(String string, Exception parent) {
		super(string, parent);
	}

	/**
	 * Instantiates a new async task exception.
	 *
	 * @param parent
	 *            the parent
	 */
	public AsyncTaskException(Exception parent) {
		super(parent);
	}
}
