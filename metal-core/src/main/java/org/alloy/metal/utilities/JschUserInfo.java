package org.alloy.metal.utilities;

import com.jcraft.jsch.UserInfo;

public class JschUserInfo implements UserInfo {
	private String password;

	public JschUserInfo(String password) {
		this.password = password;
	}

	@Override
	public String getPassphrase() {
		return password;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean promptPassword(String message) {
		return true;
	}

	@Override
	public boolean promptPassphrase(String message) {
		return true;
	}

	@Override
	public boolean promptYesNo(String message) {
		return false;
	}

	@Override
	public void showMessage(String message) {

	}
}