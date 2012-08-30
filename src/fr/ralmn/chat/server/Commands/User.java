package fr.ralmn.chat.server.Commands;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import fr.ralmn.chat.server.Reader;
import fr.ralmn.chat.server.Server;
import fr.ralmn.chat.server.ServerMain;
import fr.ralmn.chat.server.Util;

@SuppressWarnings("deprecation")
public class User extends Sender {

	private String pwd;
	private String display;
	private Socket socket;
	private boolean admin;
	private boolean vip;
	private boolean notacces;
	private boolean logged;
	private Thread sender;
	private Thread reader;
	private PrintWriter out;
	private BufferedReader in;

	public User(String user, Socket s) {
		super(user);
		if (getsAdmins().containsKey(user) || getsVip().containsKey(user)) {
			try {
				PrintWriter out = new PrintWriter(s.getOutputStream());
				notacces = true;
				out.println("notperms");
				out.flush();
				s.close();
				s = null;
			} catch (IOException e) {
				e.printStackTrace();
			}

			return;
		} else {
			notacces = false;
			admin = false;
			socket = s;
			this.user = user;
			this.display = user;
			
			if (isBanned()) {
				try {
					PrintWriter out = new PrintWriter(s.getOutputStream());
					notacces = true;
					out.println("ban");
					out.flush();
					System.out.println(user
							+ " tente de se connecter alors qu'il est bannis");
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			
			Server.getInstance().users.add(this);
			randomDisplayName();

		}
	}

	public User(String user, String pwd, Socket s) {
		super(user);
		if (getsAdmins().containsKey(user) && !checkadmin(user, pwd)) {
			try {
				PrintWriter out = new PrintWriter(s.getOutputStream());
				notacces = true;
				out.println("notperms");
				out.flush();
				s.close();
				s = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		} else if (getsVip().containsKey(user) && !checkVip(user, pwd)) {
			try {
				notacces = true;
				PrintWriter out = new PrintWriter(s.getOutputStream());
				out.println("notperms");
				out.flush();
				s.close();
				s = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		} else {
			notacces = false;
			admin = checkadmin(user, pwd);
			vip = checkVip(user, pwd);
			socket = s;
			this.user = user;
			logged = true;
			System.out.println("Le user est \"Logged\"");
			this.pwd = pwd;
			Server.getInstance().users.add(this);
			this.display = user;
			if (admin) {
				this.display = "<b> <font color=red>" + user + "</font></b>";
				PrintWriter out;
				try {
					System.out.println(user + " est admin");
					out = new PrintWriter(s.getOutputStream());
					out.println("Tu est admin");
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else if (vip) {
				this.display = "<b> <font color=F400BB>" + user + "</font></b>";
				PrintWriter out;
				try {
					System.out.println(user + " est VIP");
					out = new PrintWriter(s.getOutputStream());
					out.println("Tu est VIP");
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
	}

	private void randomDisplayName() {

		int id = (int) (Math.random() * (3 - 1)) + 1;
		System.out.println(id);
		if (id == 1) {
			this.display = "<b> <font color=green>" + this.user + "</font></b>";
		} else if (id == 2) {
			this.display = "<b> <font color=blue>" + this.user + "</font></b>";
		} else {
			this.display = "<b> <font color='#F8FF36' >" + this.user
					+ "</font></b>";
		}
	}

	public PrintWriter getOut() {
		return out;	
	}

	public BufferedReader getIn() {
		return in;
	}



	public Socket getSocket() {
		return socket;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}


	public String getDisplayName() {
		return this.display;
	}

	private boolean checkadmin(String user, String pwd) {

		HashMap<String, String> admins = getsAdmins();

		if (admins.containsKey(user)) {

			if (admins.get(user).equals(pwd)) {
				return true;
			} else {
				return false;
			}
		} else
			return false;

	}

	private boolean checkVip(String user, String pwd) {

		if (getsVip().containsKey(user)) {
			if (getsVip().get(user).equals(pwd)) {
				return true;
			} else
				return false;
		} else
			return false;

	}

	private HashMap<String, String> getsVip() {
		return Util.getVIPWF();
	}

	private HashMap<String, String> getsAdmins() {
		return Util.getAdminsWF();
	}
	
	
	

	public void diconecting() {
		notifyDeconextion();
		ServerMain.d(this);
		
		sender.stop();
		reader.stop();
	
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void notifyDeconextion() {

		for (User u : Server.getInstance().users) {
			u.sendMessage(this, "Disconnecting");
		}
		System.out.println(this.user + " Disconnecting");
	}

	private void notifyConextion() {

		for (User u : Server.getInstance().users) {
			u.sendMessage(this, "Connection");
		}

	}

	public void connecting() throws IOException {
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());
		System.out
				.println("Lancement du reader et du sender pour l'utilisateur : "
						+ this.getUser());
		sender = new Thread(new fr.ralmn.chat.server.Sender(out, this));
		sender.start();
		reader = new Thread(new Reader(this, in));
		reader.start();
		socket.setSoTimeout(30000);
		notifyConextion();
	}

	public void exit() throws IOException {
		ServerMain.d(this);
		socket.shutdownInput();
		socket.shutdownOutput();

		socket.close();

		notifyDeconextion();
	}

	public static User getUser(String name) {
		User u = null;

		for (User us : Server.getInstance().users) {
			if (us.getUser().startsWith(name)) {
				u = us;
				break;
			}
		}
		return u;
	}

	public void kick() {
		ServerMain.d(this);
		
		getOut().println("[kick]");
		getOut().flush();
		sender.stop();
		reader.stop();
		notifyKick();
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void notifyKick() {
		for (User u : Server.getInstance().users) {
			u.sendMessage(this, "Kicked");
		}
		System.out.println(this.user + " Kick");

	}

	public void timeout() {
		notifyTimeOut();
		ServerMain.d(this);
		sender.stop();
		reader.stop();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void notifyTimeOut() {
		for (User u : Server.getInstance().users) {
			u.sendMessage(this, "TimeOut");
		}
		System.out.println(this.user + " TimeOut");
	}

	public boolean isNotAcces() {
		return notacces;
	}

	public boolean isBanned() {
		boolean r = false;
		File f = new File(Util.getWorkDir(), "/banned_players.txt");
		try {
			if (!f.exists())
				f.createNewFile();

			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			String line = "";

			while ((line = br.readLine()) != null) {

				if (line.equals(this.user)) {
					r = true;
					break;
				}
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return r;
	}

	public void setBanned(boolean b) {
		try {
			File f = new File(Util.getWorkDir(), "/banned_players.txt");
			if (b) {

				if (!isBanned()) {

					FileWriter fw = new FileWriter(f, true);
					BufferedWriter bw = new BufferedWriter(fw);

					bw.write(user);

					bw.newLine();
					bw.close();
					fw.close();

				} else {
					if (!isBanned()) {
						return;
					} else {
						ArrayList<String> list = new ArrayList<String>();
						FileReader fr = new FileReader(f);
						BufferedReader br = new BufferedReader(fr);

						String line = "";

						while ((line = br.readLine()) != null) {
							list.add(line);
						}
						list.remove(user);

						FileWriter fw = new FileWriter(f, false);
						BufferedWriter bw = new BufferedWriter(fw);

						for (String s : list) {

							bw.write(s);
							bw.newLine();

						}

						bw.close();
						fw.close();

					}
				}

			} else {

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void unban() {
		setBanned(false);
		notifyUnBan();
	}

	public void ban() {
		
		
		getOut().println("[ban]");
		getOut().flush();
		sender.stop();
		reader.stop();
		notifyBan();
		setBanned(true);
		ServerMain.d(this);
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void notifyBan() {
		for (User u : Server.getInstance().users) {
			u.sendMessage(this, "Banned");
		}
		System.out.println(this.user + " Banned");

	}

	private void notifyUnBan() {
		for (User u : Server.getInstance().users) {
			u.sendMessage(this, "Banned");
		}
		System.out.println(this.user + " Banned");

	}

	public static void setBanned(boolean b, String user) {
		try {
			File f = new File(Util.getWorkDir(), "/banned_players.txt");

			ArrayList<String> list = new ArrayList<String>();
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			String line = "";

			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			f.delete();
			br.close();
			fr.close();
			if (list.contains(user)) {

				if (b) {

				} else {
					list.remove(user);
				}

			} else {
				if (b) {
					list.add(user);
				} else {
				}
			}

			f.createNewFile();

			FileWriter fw = new FileWriter(f, false);
			BufferedWriter bw = new BufferedWriter(fw);

			for (String s : list) {
				bw.write(s);
				bw.close();
			}
			bw.close();
			fw.close();

			System.out.println(user + " ban : " + b);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isLogged() {
		return this.logged;
	}

	public void addVip() {
		if (isLogged() && !vip) {
			getsVip().put(user, pwd);
			vip = true;
			this.display = "<b> <font color=F400BB>" + user + "</font></b>";
			Util.saveVipList(getsVip());
		}
	}

	public void remVip() {
		if (isLogged() && vip) {
			getsVip().remove(user);
			vip = false;
			randomDisplayName();
			Util.saveVipList(getsVip());
		}
	}

	public void addAdmin() {
		if (isLogged()) {
			getsAdmins().put(user, pwd);
			admin = true;
			this.display = "<b> <font color=red>" + user + "</font></b>";
			Util.saveAdminList(getsAdmins());
		}
	}

	public void remAdmin() {
		if (isLogged() && admin) {
			getsAdmins().remove(user);
			admin = false;
			randomDisplayName();
			Util.saveAdminList(getsAdmins());
		}
	}
	
	
	public void sendMessage(User u, String m) {
		PrintWriter out;
		try {
			m = m.replace("<", "&lt;");
			m = m.replace(">", "&gt;");
			out = new PrintWriter(socket.getOutputStream());
			String msg = u.getDisplayName() + " : " + m;
			out.println(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void sendMessageServer(String m) {
		String msg = "<font colo=red><b> Serveur </b></font>"+  ": " + m;
		out.println(msg);
		out.flush();
	}

}
