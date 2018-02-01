package classes.dialog;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * Diese Klasse zeigt einen Fortschritts-Dialog an
 * @author Oliver Streuli
 * @see Dialog
 * @see Observer
 */
public class ProgressDialog extends Dialog implements Observer {
	
	private static final long serialVersionUID = 1L;
	
	private Object currentItem;
	private int currentValue;
	
	private int maxValue;
	private JProgressBar progressBar;
	private JLabel text;
	
	/**
	 * @param owner Das JFrame, das den Dialog anzeigt
	 * @param title Der Titel des Dialoges
	 */
	public ProgressDialog(JFrame owner, String title) {
		super(owner, title);
		
		this.setSize(400, 100);
		this.setLayout(null);
		
		this.progressBar = new JProgressBar();
		this.progressBar.setBounds(10, 10, 370, 30);
		this.add(progressBar);
		
		this.text = new JLabel();
		this.text.setBounds(10, 40, 370, 30);
		this.add(text);
	}
	
	@Override
	public void dispose() {
		// TODO
	}
	
	public Object getCurrentItem() {
		return currentItem;
	}
	
	public int getCurrentValue() {
		return currentValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void increaseCurrentValue() {
		this.setCurrentValue(getCurrentValue() + 1);
	}

	public void setCurrentItem(Object currentItem) {
		this.currentItem = currentItem;
	}
	
	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	public void setMaxValue(int maxValue) {
		this.setCurrentValue(0);
		this.maxValue = maxValue;
		this.progressBar.setMaximum(maxValue);
	}

	@Override
	public boolean showDialog() {
		// TODO
		return isVisible();
	}
	
	@Override
	/**
	 * Updated den Fortschritts-Dialog
	 */
	public void update(Observable o, Object arg) {
		this.progressBar.setValue(getCurrentValue());
		this.text.setText(getCurrentItem().toString());
		
		// Der Dialog wird zur Zeit nur auf der Konsole angezeigt
		// Die Zeit für eine sinvolle Implementierung diese Dialoges war nicht vorhanden
		System.out.println("--------------------------------");
		System.out.println(getTitle());
		System.out.println(getCurrentValue() + " / " + getMaxValue());
		System.out.println(getCurrentItem().toString());
		System.out.println("--------------------------------");
	}	
}
