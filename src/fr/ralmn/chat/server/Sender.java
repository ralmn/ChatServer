package fr.ralmn.chat.server;

import java.io.PrintWriter;
import java.util.Scanner;

import fr.ralmn.chat.server.Commands.User;

@SuppressWarnings("unused")
public class Sender extends Thread {

	private PrintWriter out;

	private String message = null;
	private Scanner sc = null;
	private User user;

	public Sender(PrintWriter o, User u) {
		out = o;
		user = u;
	}


	public void run() {

		sc = new Scanner(System.in);

		while (true) {
		}
	}
}
