package view;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import model.ResizableImage;

import org.jdom.Element;

import settings.Settings;
import classes.GridBagHelper;
import classes.dialog.Dialog;
import classes.dialog.InputDialog;
import classes.dialog.MessageDialog;
import classes.dialog.ProgressDialog;
import controller.MainController;

/**
 * Diese Klasse beinhaltet das Hauptfenster
 * @author Oliver Streuli
 * @see JFrame
 *
 */
public class MainView extends JFrame {

	private static MainView mainViewObject;
	private static final long serialVersionUID = 1L;
	
	private JPanel eastPanels;
	private final Dialog errorDialog;
	private HashMap<String, String> errorMessages;
	private final Dialog informationDialog;
	private HashMap<String, String> informationMessages;
	private final InputDialog inputDialog;
	private HashMap<String, String> inputMessages;
	
	private final ListView listView;
	
	private final MenuBarView menuBarView;
	private final NameView nameView;
	private final OptionsView optionsView;
	private final ProgressDialog progressDialog;
	private final Dialog questionDialog;
	private HashMap<String, String> questionMessages;
	
	private JButton run;
	private final SizeView sizeView;
	
	/**
	 * @param mainController Der Haupt-Kontroller
	 */
	private MainView(MainController mainController) {	
		this.setTitle("r3zn1k.ch ~ ImageResizer");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setMinimumSize( new Dimension(700, 660) );
		this.setResizable(true);
		
		GridBagLayout mainLayout = new GridBagLayout();
		this.getContentPane().setLayout(mainLayout);
		
		// Die einzelnen Panels werden geholt
		this.menuBarView = mainController.getMenuBarView();
		this.listView = mainController.getListView();
		this.sizeView = mainController.getSizeView();
		this.nameView = mainController.getNameView();
		this.optionsView = mainController.getOptionsView();
		
		// Das Menu wird hinzugefügt
		this.setJMenuBar(menuBarView);
		
		// Die einzelnen Dialoge werden erstellt
		this.progressDialog = new ProgressDialog(this, "resizeImages");
		this.informationDialog = new MessageDialog("Information", "This is an Information", MessageDialog.INFORMATION, this);
		this.errorDialog = new MessageDialog("Error", "Couldn't load language", MessageDialog.ERROR, this);
		this.questionDialog = new MessageDialog("Question", "Yes or No?", MessageDialog.QUESTION, this);
		this.inputDialog = new InputDialog("Input", "Enter a text");
		
		// Die einzelnen Dialog-Nachrichten werden initialisiert
		this.informationMessages = new HashMap<String, String>();
		this.errorMessages = new HashMap<String, String>();
		this.questionMessages = new HashMap<String, String>();
		this.inputMessages = new HashMap<String, String>();
		
		// Der "Ausführen"-Button wird erstellt
		this.run = new JButton("run");
		this.run.setActionCommand("run");
		this.run.addActionListener(mainController.new ButtonListener());
		
		// Das Panel für die Panels auf der rechten Seite wird erstellt
		GridBagLayout panelLayout = new GridBagLayout();
		this.eastPanels = new JPanel(panelLayout);
		
		// Die Eigenschaften der Panels werden gesetzt
		panelLayout.setConstraints(sizeView, GridBagHelper.setGridBagConstraints(0, 0, false));
		panelLayout.setConstraints(nameView, GridBagHelper.setGridBagConstraints(0, 1, false));
		panelLayout.setConstraints(optionsView, GridBagHelper.setGridBagConstraints(0, 2, false));
		panelLayout.setConstraints(run, GridBagHelper.setGridBagConstraints(0, 3, false));
		
		// Die Grösse der einezlen Panels wird gesetzt
		sizeView.setPreferredSize(new Dimension(250, 225));
		nameView.setPreferredSize(new Dimension(250, 100));
		optionsView.setPreferredSize(new Dimension(250, 170));
		run.setPreferredSize(new Dimension(250, 100));
		
		// Die Panels werden zu einem Haupt-Panel hinzugefügt
		this.eastPanels.add(sizeView);
		this.eastPanels.add(nameView);
		this.eastPanels.add(optionsView);
		this.eastPanels.add(run);
		
		// Die Bilderliste und das Haupt-Panel, das die Panels auf der rechten Seite enthält, werden zum Hauptfenster hinzugefügt
		mainLayout.setConstraints(listView, GridBagHelper.setGridBagConstraints(0, 0));
		mainLayout.setConstraints(eastPanels, GridBagHelper.setGridBagConstraints(1, 0, false, true));
		this.eastPanels.setMinimumSize(new Dimension(250, 660));
		this.add(listView);
		this.add(eastPanels);
		
		// Die Aktion, damit das Fenster beim drücken der "Escape-Taste" geschlossen wird, wird erstellt
		KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
		Action escapeAction = mainController.new EscapeAction();
		this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKey, "ESCAPE");
		this.getRootPane().getActionMap().put("ESCAPE", escapeAction);
	}

	@Override
	public void dispose() {
		super.dispose();
		Settings.saveSettings();
	}
	
	public ListView getListView() {
		return listView;
	}
	
	public MenuBarView getMenuBarView() {
		return menuBarView;
	}
	
	public NameView getNameView() {
		return nameView;
	}
	
	public OptionsView getOptionsView() {
		return optionsView;
	}
	
	public Observer getProgressDialog() {
		return progressDialog;
	}
	
	public ArrayList<ResizableImage> getSelectedImages() {
		return listView.getSelectedImages();
	}
	
	public SizeView getSizeView() {
		return sizeView;
	}
	
	/**
	 * Diese Methode lädt die Konfiguration
	 */
	public void loadConfiguration() {
		if (!Settings.getImages().isEmpty())
			this.listView.loadConfiguration(showQuestionDialog("loadLastImages"));
		this.sizeView.loadConfiguration();
		this.nameView.loadConfiguration();
		this.optionsView.loadConfiguration();
	}
	
	/**
	 * Diese Methode lädt den Text
	 */
	public void loadText() {
		if (!Settings.loadLanguage()) {
			this.showErrorDialog("");
			return;
		}
		
		Element panels = Settings.getLanguageFile().getRootElement().getChild("panels");
		{ 
			run.setText(panels.getChild("run").getText());
		}
		Element messageDialogs = Settings.getLanguageFile().getRootElement().getChild("messageDialogs");
		{
			informationDialog.setTitle(messageDialogs.getChild("information").getAttributeValue("name"));
			informationMessages.clear();
			informationMessages.put("notAvailable", messageDialogs.getChild("information").getChild("notAvailable").getText());
			informationMessages.put("noImagesSelected", messageDialogs.getChild("information").getChild("noImagesSelected").getText());
			informationMessages.put("successfulResize", messageDialogs.getChild("information").getChild("successfulResize").getText());
			
			errorDialog.setTitle(messageDialogs.getChild("error").getAttributeValue("name"));
			errorMessages.clear();
			errorMessages.put("language", messageDialogs.getChild("error").getChild("language").getText());
			errorMessages.put("emptyInputs", messageDialogs.getChild("error").getChild("emptyInputs").getText());
			errorMessages.put("noOutputDir", messageDialogs.getChild("error").getChild("noOutputDir").getText());
			errorMessages.put("renameName", messageDialogs.getChild("error").getChild("renameName").getText());
			errorMessages.put("renameIndex", messageDialogs.getChild("error").getChild("renameIndex").getText());
			errorMessages.put("resize", messageDialogs.getChild("error").getChild("resize").getText());
			errorMessages.put("rename", messageDialogs.getChild("error").getChild("rename").getText());
			errorMessages.put("openImage", messageDialogs.getChild("error").getChild("openImage").getText());
			errorMessages.put("renameImage", messageDialogs.getChild("error").getChild("renameImage").getText());
			
			questionDialog.setTitle(messageDialogs.getChild("question").getAttributeValue("name"));
			questionMessages.clear();
			questionMessages.put("outputDir", messageDialogs.getChild("question").getChild("outputDir").getText());		
			questionMessages.put("loadLastImages", messageDialogs.getChild("question").getChild("loadLastImages").getText());
			questionMessages.put("overwriteImages", messageDialogs.getChild("question").getChild("overwriteImages").getText());
			
			inputDialog.setTitle(messageDialogs.getChild("input").getAttributeValue("name"));
			inputMessages.clear();
			inputMessages.put("renameImage", messageDialogs.getChild("input").getChild("renameImage").getText());		
		}
		Element progressDialogs = Settings.getLanguageFile().getRootElement().getChild("progressDialogs");
		{
			progressDialog.setTitle(progressDialogs.getChild("resizeImages").getText());
		}
		
		this.menuBarView.loadText();
		this.listView.loadText();
		this.sizeView.loadText();
		this.nameView.loadText();
		this.optionsView.loadText();
	}
	
	/**
	 * Diese Methode überprüft die Benutzereingaben und speichert sie
	 * @return Gibt <code>true</code> zurück, falls die Eingaben valid sind
	 */
	public boolean saveUserEntries() {
		boolean ret = false;
		boolean sizeRet = sizeView.saveEnterdSize();
		boolean nameRet = nameView.saveEnterdName();
		
		ret = sizeRet;
		ret = Settings.isRename() ? nameRet : ret;	
		return ret;
	}
	
	/**
	 * Zeigt eine Fehlermeldung an
	 * @param message Fehlermeldung
	 */
	public void showErrorDialog(String message) {
		if (errorMessages.containsKey(message)) {
			errorDialog.setMessage(errorMessages.get(message));
		}
		errorDialog.showDialog();
	}
	
	/**
	 * Zeigt eine Information an
	 * @param message Information
	 */
	public void showInformationDialog(String message) {
		if (informationMessages.containsKey(message)) {
			informationDialog.setMessage(informationMessages.get(message));
		}
		informationDialog.showDialog();
	}
	
	/**
	 * Zeigt einen Dialog an, der eine Eingabe verlang
	 * @param message Die Nachricht die angezeigt werden soll
	 * @return Gibt die Eingabe zurück
	 */
	public String showInputDialog(String message) {
		if (inputMessages.containsKey(message)) {
			inputDialog.setMessage(inputMessages.get(message));
		}
		return inputDialog.showInputDialog();
	}

	/**
	 * Zeigt einen Fortschritts-Dialog an
	 * @param show Falls <code>true</code> angegeben ist, wird der Dialog angezeigt, ansonsten geschlossen
	 * @param maxValue Der Maximalwert der Fortschritts-Anzeige
	 * @return gibt <code>true</code> zurück, falls der Dialog angezeigt wird
	 */
	public boolean showProgressDialog(boolean show, int maxValue) {
		boolean ret = true;
		if (show) {
			progressDialog.setMaxValue(maxValue);
			ret = progressDialog.showDialog();
		}
		else {
			progressDialog.dispose();
		}
		return ret;
	}
	
	/**
	 * Zeigt einen Dialog an, der eine Frage stellt
	 * @param message Information
	 */
	public boolean showQuestionDialog(String message) {
		if (questionMessages.containsKey(message)) {
			questionDialog.setMessage(questionMessages.get(message));
		}
		return questionDialog.showDialog();
	}
	
	/**
	 * Erneuert den Fortschritts-Dialog
	 * @param currentValue Den aktuellen Wert
	 * @param currentItem Das aktuell bearbeitetete Item
	 */
	public void updateProgressDialog(int currentValue, String currentItem) {
		progressDialog.setCurrentValue(currentValue);
		progressDialog.setCurrentItem(currentItem);
	}
	
	/**
	 * Singleton
	 */
	public static MainView getMainView(MainController mainController) {
		if (mainViewObject == null) {
			mainViewObject = new MainView(mainController);
		}
		mainViewObject.loadText();
		mainViewObject.loadConfiguration();
		mainViewObject.setVisible(true);
		mainViewObject.requestFocus();
		return mainViewObject;
	}	
}
