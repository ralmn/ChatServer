package fr.ralmn.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Login extends Thread {

	private ServerSocket socketserver = null;
	private Socket socket = null;

	public Thread t1;

	public Login(ServerSocket s) {
		socketserver = s;
	}

	public void run() {
		try {
			while (true) {
				socket = socketserver.accept();
				System.out.print("Un utilisateur veut se connecter ");

				t1 = new Thread(new Auth(socket));
				t1.start();

			}
		} catch (IOException e) {

			System.err.println("Erreur serveur");
		}

	}

}
