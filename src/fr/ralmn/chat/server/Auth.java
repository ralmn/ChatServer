package fr.ralmn.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import fr.ralmn.chat.server.Commands.User;

public class Auth extends Thread {

	private Socket socket;
	private PrintWriter out = null;
	private BufferedReader in = null;
	public boolean authentifier = false;
	@SuppressWarnings("unused")
	private String login, pwd;

	public Auth(Socket s) {
		socket = s;
	}

	public void run() {

		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());

			while (!authentifier) {

				out.println("Login");
				out.flush();

				String lo = in.readLine();
				System.out.println(lo);
				User user;
				lo = lo.replace("[LOG]", "");
				String pa = in.readLine();
				pa = pa.replace("[LOG]", "");

				if (ServerMain.isConnecting(lo)) {
					out.println("already");
					out.flush();
					System.out.println("sauf que son pseudo est pris");
					authentifier = true;
					socket.close();
					socket = null;
					break;
				}

				if (pa.equalsIgnoreCase("pass")) {
					user = new User(lo, socket);
				} else {
					user = new User(lo, pa, socket);
				}

				if (user.isNotAcces()) {
					authentifier = true;
					System.out.println(" sauf qu'il n'a pas le droit");
					break;
				} else if (user.isBanned()) {
					try {
						authentifier = true;
						Server.getInstance().users.remove(user);
						PrintWriter out = new PrintWriter(user.getSocket()
								.getOutputStream());
						System.out
								.println(user
										+ " tente de se connecter alors qu'il est bannis");
						out.println("ban");
						out.flush();
						user.getSocket().close();
						user = null;
						break;
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				System.out.println("");
				out.println("accept");
				System.out.println(user.getUser() + " vient de se connecter ");
				out.flush();
				authentifier = true;
				Thread t2 = new Thread(new ServerTask(user, socket));
				t2.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
