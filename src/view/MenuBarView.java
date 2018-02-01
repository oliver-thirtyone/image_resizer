package view;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.jdom.Element;

import settings.Settings;
import controller.MenuBarController;

/**
 * Diese Klasse zeigt die Menuleiste an
 * @author Oliver Streuli
 * @see JMenuBar
 */
public class MenuBarView extends JMenuBar {
	
	private static final long serialVersionUID = 1L;
	
	private JMenuItem about;
	private JMenuItem addImages;
	private JMenuItem askBeforeRun;
	private JMenuItem clearList;
	private JMenuItem deselectAll;
	private JMenuItem dontOverwriteImages;
	
	private JMenu edit;
	private JMenuItem english;
	private JMenuItem exit;
	private JMenu file;

	private JMenuItem german;
	private JMenu help;
	private JMenu language;
	private JMenuItem manual;
	private JMenu options;
	private JMenuItem overwriteImages;
	
	private JMenu overwriteImagesMenu;
	private JMenuItem selectAll;
	private JMenuItem selectOutputDir;
	private JMenuItem visit;
	
	/**
	 * @param controller Der Controller diese Panels
	 */
	public MenuBarView(MenuBarController controller) {
		// Datei
		{
			file = new JMenu("file");
			this.add(file);
			{
				addImages = new JMenuItem("addImages");
				addImages.setActionCommand("addImages");
				addImages.addActionListener(controller);
				addImages.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
				file.add(addImages);
			}
			{
				language = new JMenu("language");
				file.add(language);
				{
					german = new JMenuItem("language.german");
					german.setActionCommand("language.german");
					german.addActionListener(controller);
					language.add(german);
				}
				{
					english = new JMenuItem("language.english");
					english.setActionCommand("language.english");
					english.addActionListener(controller);
					language.add(english);
				}
			}
			{
				exit = new JMenuItem("exit");
				exit.setActionCommand("exit");
				exit.addActionListener(controller);
				exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false));
				file.add(exit);
			}
		}
		// Bearbeiten
		{
			edit = new JMenu("edit");
			this.add(edit);
			{
				selectAll = new JMenuItem("selectAll");
				selectAll.setActionCommand("selectAll");
				selectAll.addActionListener(controller);
				selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
				edit.add(selectAll);
			}
			{
				deselectAll = new JMenuItem("deselectAll");
				deselectAll.setActionCommand("deselectAll");
				deselectAll.addActionListener(controller);
				deselectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
				edit.add(deselectAll);
			}
			{
				clearList = new JMenuItem("clearList");
				clearList.setActionCommand("clearList");
				clearList.addActionListener(controller);
				clearList.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
				edit.add(clearList);
			}
		}
		// Optionen
		{
			options = new JMenu("options");
			this.add(options);
			{
				overwriteImagesMenu = new JMenu("overwriteImages");
				options.add(overwriteImagesMenu);
				{
					overwriteImages = new JMenuItem("overwriteImages");
					overwriteImages.setActionCommand("overwriteImages");
					overwriteImages.addActionListener(controller);
					overwriteImagesMenu.add(overwriteImages);
				}
				{
					askBeforeRun = new JMenuItem("askBeforeRun");
					askBeforeRun.setActionCommand("askBeforeRun");
					askBeforeRun.addActionListener(controller);
					overwriteImagesMenu.add(askBeforeRun);
				}
				{
					dontOverwriteImages = new JMenuItem("dontOverwriteImages");
					dontOverwriteImages.setActionCommand("dontOverwriteImages");
					dontOverwriteImages.addActionListener(controller);
					overwriteImagesMenu.add(dontOverwriteImages);
				}
			}
			{
				selectOutputDir = new JMenuItem("selectOutputDir");
				selectOutputDir.setActionCommand("selectOutputDir");
				selectOutputDir.addActionListener(controller);
				selectOutputDir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
				options.add(selectOutputDir);
			}
		}
		// Hilfe
		{
			help = new JMenu("help");
			this.add(help);
			{
				manual = new JMenuItem("manual");
				manual.setActionCommand("manual");
				manual.addActionListener(controller);
				help.add(manual);
			}
			{
				about = new JMenuItem("about");
				about.setActionCommand("about");
				about.addActionListener(controller);
				help.add(about);
			}
			{
				visit = new JMenuItem("visit");
				visit.setActionCommand("visit");
				visit.addActionListener(controller);
				help.add(visit);
			}
		}
	}
	
	/**
	 * Diese Methode lädt den Text
	 */
	public void loadText() {
		Element menuBar = Settings.getLanguageFile().getRootElement().getChild("menuBar"); 
		Element panels = Settings.getLanguageFile().getRootElement().getChild("panels"); 
		{
			file.setText(menuBar.getChild("file").getAttributeValue("name"));
			addImages.setText(panels.getChild("imageList").getChild("addImages").getText());
			language.setText(menuBar.getChild("file").getChild("language").getAttributeValue("name"));
			german.setText(menuBar.getChild("file").getChild("language").getChild("german").getText());
			english.setText(menuBar.getChild("file").getChild("language").getChild("english").getText());
			exit.setText(menuBar.getChild("file").getChild("exit").getText());
		}
		{
			edit.setText(menuBar.getChild("edit").getAttributeValue("name"));
			selectAll.setText(panels.getChild("imageList").getChild("selectAll").getText());
			deselectAll.setText(panels.getChild("imageList").getChild("deselectAll").getText());
			clearList.setText(panels.getChild("imageList").getChild("clearList").getText());
		}
		{
			options.setText(panels.getChild("options").getAttributeValue("name"));
			overwriteImagesMenu.setText(panels.getChild("options").getChild("overwriteImages").getAttributeValue("name"));
			overwriteImages.setText(panels.getChild("options").getChild("overwriteImages").getChild("overwriteImages").getText());
			askBeforeRun.setText(panels.getChild("options").getChild("overwriteImages").getChild("askBeforeRun").getText());
			dontOverwriteImages.setText(panels.getChild("options").getChild("overwriteImages").getChild("dontOverwriteImages").getText());
			selectOutputDir.setText(panels.getChild("options").getChild("outputDir").getText());
		}
		{
			help.setText(menuBar.getChild("help").getAttributeValue("name"));
			manual.setText(menuBar.getChild("help").getChild("manual").getText());
			about.setText(menuBar.getChild("help").getChild("about").getText());
			visit.setText(menuBar.getChild("help").getChild("visit").getText());
		}
	}
}
