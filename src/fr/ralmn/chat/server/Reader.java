package fr.ralmn.chat.server;

import java.io.BufferedReader;
import java.io.IOException;

import fr.ralmn.chat.server.Commands.CommandManager;
import fr.ralmn.chat.server.Commands.User;

public class Reader extends Thread {

	private BufferedReader in;
	private String message = null;
	private User user;

	public Reader(User u, BufferedReader i) {
		in = i;
		user = u;
	}

	@SuppressWarnings("deprecation")
	public void run() {

		while (true) {

			try {

				message = in.readLine();
				if (message == null) {

					user.timeout();
					
					break;
				}
				if (message.startsWith("[T]")) {
					
				} else if (message.startsWith("[LOG]")) {

				} else if (message.equals("[END]")) {
					user.diconecting();
				} else if (message.startsWith("/")) {
					CommandManager commandManage = CommandManager.getInstance();
					
					commandManage.execute(message, user);

				} else {
					for (User s : Server.getInstance().users) {
						s.sendMessage(user, message);
					}
				}
			} catch (IOException e) {
				// e.printStackTrace();
				try {
					user.exit();
					this.stop();
					user.getSocket().close();
					System.out.println(user.getDisplayName() + " Deconection");
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

		}

	}
}
