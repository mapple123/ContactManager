package main;

import java.io.File;
import java.io.IOException;

import gui.MainFrame;

public class MainMethod {
	public static String userHomeDir = "";
	public static final String DIR = "\\Kontaktverwaltung\\Kontakte";
	public static final String FILE = "\\Kontaktdaten.txt";

	public static void main(String[] args) {
		userHomeDir = System.getenv("USERPROFILE");

		File contactDataFile = new File(userHomeDir + DIR + FILE);
		if (!contactDataFile.exists()) {
			File dirFile = new File(userHomeDir + DIR);
			dirFile.mkdirs();
			try {
				contactDataFile.createNewFile();
				System.out.println("erstellt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		new MainFrame(contactDataFile);

	}

}
