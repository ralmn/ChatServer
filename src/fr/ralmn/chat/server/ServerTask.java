package fr.ralmn.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import fr.ralmn.chat.server.Commands.User;

@SuppressWarnings("unused")
public class ServerTask extends Thread {

	private Socket socket;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private User user;

	public ServerTask(User u, Socket s) {
		socket = s;
		user = u;
	}

	public void run() {

		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			
			user.connecting();

		} catch (IOException e) {
			System.err.println(user.getUser() + "s'est déconnecté ");
			user.diconecting();
		}

	}

}
