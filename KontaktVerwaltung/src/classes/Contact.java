package classes;

import java.io.Serializable;

/**
 * Kontaktklasse für das Repräsentieren eines Kontaktes
 *
 * Entwickler: Jan Schwenger
 */

public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;
    private String firstName, lastName, address, phone, mail, imgPath;
    private long id;

    public Contact(String firstName, String lastName, String address, String phone, String mail, String imgPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.mail = mail;
        this.imgPath = imgPath;
    }

    public Contact(String firstName, String lastName, String address, String phone, String mail, String imgPath, long id) {
        this(firstName,lastName,address,phone,mail, imgPath);
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Vorname: " + this.firstName + ", Nachname: " + this.lastName + ", Adresse: " + this.address + ", Tel.: "
                + this.phone + ", Mail: " + this.mail + ", ID: " + this.id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Contact getContact(){
        return this;
    }
}
