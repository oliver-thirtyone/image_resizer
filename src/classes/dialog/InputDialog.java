package classes.dialog;

import javax.swing.JOptionPane;

/**
 * Diese Klasse zeigt einen Eingabe-Dialog an
 * @author Oliver Streuli
 * @see Dialog
 */
public class InputDialog extends Dialog {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @param title Der Titel des Dialoges
	 * @param message DIe Nachricht die angezeigt wird
	 */
	public InputDialog(String title, String message) {
		super(title, message);
	}
	
	/**
	 * Diese Methode zeigt den Eingabe-Dialog an
	 * @return Die Eingabe des Anwenders
	 */
	public String showInputDialog() {
		return JOptionPane.showInputDialog(this.getMessage());
	}

	@Override
	public boolean showDialog() {
		// TODO Auto-generated method stub
		return false;
	}
}