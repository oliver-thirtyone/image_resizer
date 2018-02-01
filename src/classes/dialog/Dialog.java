package classes.dialog;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Diese Klasse ist die abstrakte Hauptklasse für alle Dialoge
 * @author Oliver Streuli
 *
 */
public abstract class Dialog extends JDialog {
	
	private String message;
	
	/**
	 * @param owner Das JFrame, das den Dialog anzeigt
	 * @param title Der Titel, des Dialoges
	 */
	public Dialog(JFrame owner, String title) {
		super(owner, title);
	}
	
	/**
	 * @param title Der Titel, des Dialoges
	 * @param message Die Nachricht, die im Dialog angezeigt wird.
	 */
	public Dialog(String title, String message) {
		this.setTitle(title);
		this.setMessage(message);
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Diese Methode zeigt den Dialog an. Da sie abstrakt ist muss sie von den Unterklassen implementiert werden.
	 * @return Gibt <code>true</code> zurück, falls der Dialog sichtbar ist.
	 */
	public abstract boolean showDialog();
	
}
