package org.alloy.metal.json;

public class _Json {
	// FUTURE
	public static JsonStatus success() {
		JsonStatus status = new JsonStatus();
		status.setMessage("success");
		return status;
	}

	// FUTURE
	public static JsonStatus failure() {
		return null;
	}

	// FUTURE
	public static JsonStatus failure(String message, Object... objects) {
		return null;
	}
}