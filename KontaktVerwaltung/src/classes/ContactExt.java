package classes;

import java.awt.Color;
import java.awt.Font;

import add.MyIcon;

/**
 * Erweiterte Kontaktklasse inklusive weiterer Attribute
 *
 * Entwickler: Jan Schwenger
 */
public class ContactExt {
    private Font font;
    private Color color;
    private MyIcon myIcon;
    private String name;
    private Contact contact;

    public ContactExt(Font font, Color color, MyIcon myIcon, String name, Contact contact) {
        this.font = font;
        this.color = color;
        this.myIcon = myIcon;
        this.name = name;
        this.contact = contact;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public MyIcon getMyIcon() {
        return myIcon;
    }

    public void setMyIcon(MyIcon myIcon) {
        this.myIcon = myIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}