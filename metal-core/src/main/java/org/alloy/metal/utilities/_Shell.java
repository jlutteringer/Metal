package org.alloy.metal.utilities;

import java.io.InputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class _Shell {
	public static void connect(Session session) {
		if (!session.isConnected()) {
			_Exception.propagate(() -> session.connect());

			if (!session.isConnected()) {
				throw new RuntimeException("Connection to " + session.getHost() + " failed!");
			}
		}
	}

	public static InputStream exec(Session session, String command) {
		connect(session);

		return _Exception.propagate(() -> {
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand("ps -ef | grep catalina");

			InputStream output = channel.getInputStream();
			channel.connect();
			return output;
		});
	}

	public static String execWait(Session session, String command) {
		return _Stream.toString(exec(session, command));
	}

	public static Session getSession(RemoteHostProperties properties) {
		JSch shell = new JSch();
		Session session = _Exception.propagate(() -> shell.getSession(properties.getUser(), properties.getLocation()));

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);

		UserInfo userInfo = new JschUserInfo(properties.getPassword());
		session.setUserInfo(userInfo);
		return session;
	}

}
