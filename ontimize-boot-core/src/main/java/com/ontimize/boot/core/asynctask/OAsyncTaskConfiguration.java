package com.ontimize.boot.core.asynctask;

import com.ontimize.jee.common.tools.CheckingTools;

public class OAsyncTaskConfiguration {

	/** The engine. */
	private IAsyncTaskStorage engine;

	/**
	 * Gets the engine.
	 *
	 * @return the engine
	 */
	public IAsyncTaskStorage getEngine() {
		CheckingTools.failIfNull(this.engine, "No async task engine defined");
		return this.engine;
	}

	/**
	 * Sets the engine.
	 *
	 * @param engine
	 *            the engine
	 */
	public void setEngine(IAsyncTaskStorage engine) {
		this.engine = engine;
	}
}
