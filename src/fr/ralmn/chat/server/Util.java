package fr.ralmn.chat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public class Util {

	public static File getWorkDir() {

		File f = new File("Server_UniversalChat");

		if (!f.exists())
			f.mkdirs();

		return f;
	}

	public static HashMap<String, String> getAdminsWF() {
		HashMap<String, String> admin = new HashMap<String, String>();
		try {
			File f = new File(getWorkDir(), "/admin.list");

			if (f.exists())
				f.createNewFile();

			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			String line = "";

			while ((line = br.readLine()) != null) {
				String[] args = line.split(":");

				admin.put(args[0], args[1]);
			}
			br.close();
			fr.close();

		} catch (Exception e) {
		}
		return admin;
	}

	public static HashMap<String, String> getVIPWF() {
		HashMap<String, String> vip = new HashMap<String, String>();
		try {
			File f = new File(getWorkDir(), "/vip.list");

			if (f.exists())
				f.createNewFile();

			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			String line = "";

			while ((line = br.readLine()) != null) {
				String[] args = line.split(":");

				vip.put(args[0], args[1]);
			}

			br.close();
			fr.close();

		} catch (Exception e) {
		}
		return vip;
	}

	public static void initFile() {

		try {
			File f = new File(getWorkDir(), "/admin.list");

			if (!f.exists())
				f.createNewFile();

			f = new File(getWorkDir(), "/vip.list");

			if (!f.exists())
				f.createNewFile();
			f = new File(getWorkDir(), "/server.properties");

			if (!f.exists())
				f.createNewFile();

			Properties p = new Properties();
			p.load(new FileInputStream(f));

			if (!p.containsKey("port")) {
				writeConfig("port", "30000");
			}

		} catch (Exception e) {
		}
	}

	public static String readConfig(String value) {
		String r = "";
		File f = new File(getWorkDir(), "server.properties");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(f));
			r = prop.getProperty(value);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return r;
	}

	public static void writeConfig(String key, String v) {

		File f = new File(getWorkDir(), "server.properties");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(f));
			prop.setProperty(key, v);
			prop.store(new FileOutputStream(f), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static int getPort() {
		int port = Integer.parseInt(readConfig("port"));
		return port;
	}

	public static void saveAdminList(HashMap<String, String> a) {
		try {
			File f = new File(getWorkDir(), "/admin.list");

			FileWriter fw = new FileWriter(f, false);
			BufferedWriter bw = new BufferedWriter(fw);
			ArrayList<String> keys = new ArrayList<String>();

			for (Iterator<String> i = a.keySet().iterator(); i.hasNext();) {
				keys.add(i.next());
			}

			for (String key : keys) {

				String b = a.get(key);

				String r = key + ":" + b;
				bw.write(r);
				bw.newLine();
			}

			bw.close();
			fw.close();

		} catch (Exception e) {
		}
	}

	public static void saveVipList(HashMap<String, String> a) {
		try {
			File f = new File(getWorkDir(), "/vip.list");

			FileWriter fw = new FileWriter(f, false);
			BufferedWriter bw = new BufferedWriter(fw);
			ArrayList<String> keys = new ArrayList<String>();

			for (Iterator<String> i = a.keySet().iterator(); i.hasNext();) {
				keys.add(i.next());
			}

			for (String key : keys) {

				String b = a.get(key);

				String r = key + ":" + b;
				bw.write(r);
				bw.newLine();
			}

			bw.close();
			fw.close();

		} catch (Exception e) {
		}
	}

}
