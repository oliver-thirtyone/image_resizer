package classes;

import java.awt.Color;
import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * Diese Klasse hilft die Eigenschaften eines <code>GirdBagLayout</code> Komponenten zu definieren
 * @author Oliver Streuli
 *
 */
public class GridBagHelper {

	/**
	 * @return Den Standard-Rahmen
	 * 
	 */
	public static Border getStandardBorder() {
		return BorderFactory.createLineBorder(Color.black);
	}
	
	/**
	 * @param gridx X-Position
	 * @param gridy Y-Position
	 * @return Die Eigenschaften eines <code>GirdBagLayout</code> Komponenten
	 * 
	 */
	public static GridBagConstraints setGridBagConstraints(int gridx, int gridy) {
		return setGridBagConstraints(gridx, gridy, 1, 1, true, true);
	}
	
	/**
	 * @param gridx X-Position
	 * @param gridy Y-Position
	 * @param resize vergrössern
	 * @return Die Eigenschaften eines <code>GirdBagLayout</code> Komponenten
	 * 
	 */
	public static GridBagConstraints setGridBagConstraints(int gridx, int gridy, boolean resize) {
		return setGridBagConstraints(gridx, gridy, 1, 1, resize, resize);
	}
	
	/**
	 * @param gridx X-Position
	 * @param gridy Y-Position
	 * @param resizeWidth Horizontal vergrössern
	 * @param resizeHeight Vertikal vergrössern
	 * @return Die Eigenschaften eines <code>GirdBagLayout</code> Komponenten
	 * 
	 */
	public static GridBagConstraints setGridBagConstraints(int gridx, int gridy, boolean resizeWidth, boolean resizeHeight) {
		return setGridBagConstraints(gridx, gridy, 1, 1, resizeWidth, resizeHeight);
	}
	
	/**
	 * @param gridx X-Position
	 * @param gridy Y-Position
	 * @param gridwidth Breite
	 * @param gridheight Höhe
	 * @return Die Eigenschaften eines <code>GirdBagLayout</code> Komponenten
	 * 
	 */
	public static GridBagConstraints setGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight) {
		return setGridBagConstraints(gridx, gridy, gridwidth, gridheight, true, true);
	}
	
	/**
	 * @param gridx X-Position
	 * @param gridy Y-Position
	 * @param gridwidth Breite
	 * @param gridheight Höhe
	 * @param resizeWidth Horizontal vergrössern
	 * @param resizeHeight Vertikal vergrössern
	 * @return Die Eigenschaften eines <code>GirdBagLayout</code> Komponenten
	 * 
	 */
	public static GridBagConstraints setGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, boolean resizeWidth, boolean resizeHeight) {
		GridBagConstraints ret = new GridBagConstraints();	
		ret.gridx = gridx;
		ret.gridy = gridy;
		ret.gridwidth = gridwidth;
		ret.gridheight = gridheight;
		ret.fill = GridBagConstraints.BOTH;
		if (resizeWidth)
			ret.weightx = 1.0;
		if (resizeHeight) 
			ret.weighty = 1.0;
		return ret;
	}
	
}
