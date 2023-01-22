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
import classes.nObj;
import gui.MainFrame;
import main.MainMethod;
import res.Consts;

public class Methods {
	public static String userHomeDir = "";
	public static final String DIR = "\\Kontaktverwaltung\\Kontakte";
	public static final String FILE = "\\Kontaktdaten.txt";

	public static void setUpMain() {
		userHomeDir = System.getenv("USERPROFILE");
		//Locale currentLocale = Locale.getDefault();
		Locale currentLocale = Locale.GERMAN;
		System.out.println(currentLocale);
		ResourceBundle bundle = ResourceBundle.getBundle(Consts.FILENAME, currentLocale);
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

		new MainFrame(contactDataFile, bundle);

	}
	public static Contact saveContact(Contact contact) {
		File file = new File(userHomeDir + DIR + FILE);
		FileWriter fw = null;
		String firstName = contact.getFirstName();
		String lastName = contact.getLastName();
		String address = contact.getAddress();
		String phone = contact.getPhone();
		String mail = contact.getMail();

		long id = getLastContactId(file);
		try {
			fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(firstName + ",");
			bw.write(lastName + ",");
			bw.write(address + ",");
			bw.write(phone + ",");
			bw.write(mail + ",");
			bw.write("" + id);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		contact.setId(id);
		return contact;
	}

	private static ArrayList<Long> getAllIds(File file) {
		ArrayList<Long> contactIdList = new ArrayList<Long>();
		try {

			String line;
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				String[] conString = line.split(",");
				contactIdList.add(Long.parseLong(conString[5]));
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return contactIdList;
	}

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

	public static ArrayList<Contact> readAllContacts(File file) {
		ArrayList<Contact> contactList = new ArrayList<Contact>();
		Contact contact = null;
		try {

			String line;
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				String[] conString = line.split(",");
				contact = new Contact(conString[0], conString[1], conString[2], conString[3], conString[4],
						Long.parseLong(conString[5]));
				contactList.add(contact);
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return contactList;
	}

	public static Contact readContact(File file, int linePosition) {

		Contact contact = null;
		String specific_line_n = "";
		try (Stream<String> all_lines = Files.lines(Paths.get(file.getAbsolutePath()))) {
			specific_line_n = all_lines.skip(linePosition - 1).findFirst().get();
			String[] sA = specific_line_n.split(",");
			contact = new Contact(sA[0], sA[1], sA[2], sA[3], sA[4], Long.parseLong(sA[5]));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return contact;
	}

	public static String readAllText(String filename) throws Exception {
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

	private static String changeFile(String path, long line) {
		String file;
		StringBuilder sb = null;
		try {
			file = readAllText(path);
			String[] arr = file.split("\n");
			sb = new StringBuilder();

			for (String s : arr) {

				String[] sA = s.split(",");

				if (sA[5].equals("" + line))
					continue;
				else
					sb.append(s + "\n");
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return sb.toString();
	}

	public static void writeAllText(String text, String fileout) {
		try {
			PrintWriter pw = new PrintWriter(fileout);
			pw.print(text);
			pw.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static void deleteItemFile(String filePath, ArrayList<nObj> items) {
		for (nObj item : items) {
			writeAllText(changeFile(filePath, item.getContact().getId()), filePath);
		}
	}

	public static void editItem(String filePath, Contact item) {

		writeAllText(changeFile2(filePath, item.getId(), item), filePath);

	}

	private static String changeFile2(String path, long line, Contact c) {
		String file;
		StringBuilder sb = null;
		try {
			file = readAllText(path);
			String[] arr = file.split("\n");
			sb = new StringBuilder();

			for (String s : arr) {

				String[] sA = s.split(",");

				if (sA[5].equals("" + line)) {
					System.err.println(s);
					sb.append(c.getFirstName() + "," + c.getLastName() + "," + c.getAddress() + "," + c.getPhone() + ","
							+ c.getMail() + "," + c.getId() + "\n");
				} else
					sb.append(s + "\n");

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return sb.toString();
	}
	
	
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
