package org.alloy.metal.object;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class WithLogger {
	protected Logger logger = LogManager.getLogger(this.getClass());
}