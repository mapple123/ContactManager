package add;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import classes.Contact;
import classes.ContactExt;
import gui.MainFrame;
import res.Consts;

import static res.Consts.DIR;
import static res.Consts.FILE;

/**
 * Allgemeine Methoden für die Funktionalität der kompletten Anwendung.
 *
 * Entwickler: Jan Schwenger
 */
public class Methods {

	// TODO: Flexiblen Pfad für das Speichern der Textdateien
	public static String userHomeDir = "";

	// Öffentliche Methode zum Konfigurieren und zum Erstellen der Ordnerstruktur der Anwendung
	public static void setUpMain() {
		userHomeDir = System.getenv("USERPROFILE");
		//Locale currentLocale = Locale.getDefault();
		Locale currentLocale = Locale.GERMAN;
		System.out.println(currentLocale);
		ResourceBundle bundle = ResourceBundle.getBundle(Consts.FILENAME, currentLocale);
		File contactDataFile = new File(userHomeDir + File.separator + DIR + File.separator +  FILE);
		if (!contactDataFile.exists()) {
			File dirFile = new File(userHomeDir + File.separator + DIR);
			dirFile.mkdirs();
			try {
				contactDataFile.createNewFile();
				System.out.println("erstellt");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		new MainFrame(contactDataFile, bundle, userHomeDir);
	}

	// Öffentliche Methode zum Speichern eines Kontaktes
	// Rückgabewert: Objekt -> Kontakt
	public static Contact saveContact(Contact contact) {
		File file = new File(userHomeDir + File.separator + DIR + File.separator + FILE);
		FileWriter fw;
		String firstName = contact.getFirstName();
		String lastName = contact.getLastName();
		String address = contact.getAddress();
		String phone = contact.getPhone();
		String mail = contact.getMail();
		String imgPath = (contact.getImgPath() == null)? "": contact.getImgPath();
		long id = getLastContactId(file);
		try {
			fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(firstName + ",");
			bw.write(lastName + ",");
			bw.write(address + ",");
			bw.write(phone + ",");
			bw.write(mail + ",");
			bw.write(imgPath + ",");
			bw.write("" + id);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		contact.setId(id);
		return contact;
	}

	// Interne Methode zum Bekommen aller Id's
	// Rückgabewert: ArrayList -> Id's
	private static ArrayList<Long> getAllIds(File file) {
		ArrayList<Long> contactIdList = new ArrayList<Long>();
		try {
			String line;
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				String[] conString = line.split(",");
				contactIdList.add(Long.parseLong(conString[6]));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contactIdList;
	}

	// Interne Methode zum Bekommen der letzten Kontakt-Id
	// Rückgabewert: long -> Id
	private static long getLastContactId(File file) {
		int c = 0;
		ArrayList<Contact> aL = readAllContacts(file);
		int aLSize = aL.size();
		if (aLSize > 0 && getAllIds(file).contains(aL.get(aLSize - 1).getId()))
			c++;
		long res = 0;
		if (aLSize > 0)
			res = aL.get(aLSize - 1).getId() + c;
		else
			res = 1;
		return res;
	}

	// Öffentliche Methode für das Auslesen aller Kontakte
	// Rückgabewert: ArrayList -> Kontakte
	public static ArrayList<Contact> readAllContacts(File file) {
		ArrayList<Contact> contactList = new ArrayList();
		Contact contact;
		try {
			String line;
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				String[] conString = line.split(",");
				contact = new Contact(conString[0], conString[1], conString[2], conString[3], conString[4],
						(conString.length < 7)? null : conString[5], Long.parseLong(conString[6]));
				contactList.add(contact);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	// TODO: Eventuell entfernen, da kein Gebrauch
	// Öffentliche Testmethode zum bekommen eines bestimmten Kontaktes
	// Rückgabewert: Objekt -> Kontakt
	public static Contact readContact(File file, int linePosition) {
		Contact contact = null;
		String specific_line_n = "";
		try (Stream<String> all_lines = Files.lines(Paths.get(file.getAbsolutePath()))) {
			specific_line_n = all_lines.skip(linePosition - 1).findFirst().get();
			String[] sA = specific_line_n.split(",");
			contact = new Contact(sA[0], sA[1], sA[2], sA[3], sA[4], sA[6], Long.parseLong(sA[5]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contact;
	}

	// Interne Methode für das Bekommen der Informationen eines Kontaktes
	// Rückgabewert: String -> gespeicherte Daten von einem Kontakt
	private static String readAllText(String filename) throws Exception {
		StringBuilder sb = new StringBuilder();
		try {
			String line;
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// Interne Methode für das Verarbeiten der Informationen eines Kontaktes
	// Rückgabewert: String -> Verarbeiteter String von den Informationen eines Kontaktes
	private static String changeFile(String path, long line) {
		String file;
		StringBuilder sb = null;
		try {
			file = readAllText(path);
			String[] arr = file.split("\n");
			sb = new StringBuilder();
			for (String s : arr) {
				String[] sA = s.split(",");
				if (sA[6].equals("" + line))
					continue;
				else
					sb.append(s + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// Interne Methode für das Erstellen einer Datei mit den Informationen eines Kontaktes in einem fetsgelegten Pfad
	private static void writeAllText(String text, String fileout) {
		try {
			PrintWriter pw = new PrintWriter(fileout);
			pw.print(text);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Öffentliche Methode, die für das Löschen der Kontakte zuständig ist
	public static void deleteItemFile(String filePath, ArrayList<ContactExt> items) {
		for (ContactExt item : items) {
			writeAllText(changeFile(filePath, item.getContact().getId()), filePath);
		}
	}

	// Öffentliche Methode, die für das Bearbeiten eines Kontakes zuständig ist
	public static void editItem(String filePath, Contact item) {
		writeAllText(changeFile2(filePath, item.getId(), item), filePath);
	}

	// Interne Methode für das Verarbeiten der Informationen eines Kontaktes
	// Rückgabewert: String -> Verarbeiteter String von den Informationen eines Kontaktes
	private static String changeFile2(String path, long line, Contact c) {
		String file;
		StringBuilder sb = null;
		try {
			file = readAllText(path);
			String[] arr = file.split("\n");
			sb = new StringBuilder();
			for (String s : arr) {
				String[] sA = s.split(",");
				if (sA[6].equals("" + line)) {
					System.err.println(s);
					sb.append(c.getFirstName() + "," + c.getLastName() + "," + c.getAddress() + "," + c.getPhone() + ","
							+ c.getMail() + "," + c.getImgPath() + "," + c.getId() + "\n");
				} else
					sb.append(s + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	

	// TODO: Weitere Funktionalität für die Konfigurationsdatei implementieren inklusive Kommentare
	public static void writeConfig(List<Contact> contacts, String mP) throws Exception
	{
		FileOutputStream file = new FileOutputStream("bgSave.enc");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(bos);
		for(Contact contact : contacts) {
			objectStream.writeObject(contact);
		}
		
		ReadWriteAES.encode(bos.toByteArray(), file, mP);
		
		objectStream.close();
	}
	
	public static ArrayList<Contact> readConfig(String mP) throws Exception
	{
		Contact config = null;
		
		FileInputStream file = new FileInputStream("bgSave.enc");
		byte[] fileContent = ReadWriteAES.decode(file, mP);
		ByteArrayInputStream bis = new ByteArrayInputStream(fileContent);
		ObjectInputStream objectStream = new ObjectInputStream(bis);
		ArrayList<Contact> users = new ArrayList<Contact>();
		//Set<User> users = new HashSet<>();
		try { 
		  for (;;) { 
			config = (Contact) objectStream.readObject();
			users.add(config);
		  }
		} catch (EOFException e) {
		} 
		return users;
	}

}
