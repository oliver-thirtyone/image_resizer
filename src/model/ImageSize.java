package model;

/**
 * Diese Klasse enthält die Grösse eines Bildes
 * @author Oliver Streuli
 *
 */
public class ImageSize {

	private static final long serialVersionUID = 1L;
	
	private Integer height;
	private Integer width;
	
	/**
	 * @param width Die Breite
	 * @param height DIe Höhe
	 */
	public ImageSize (Integer width, Integer height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	public Integer getHeight() {
		return height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}
	
	@Override
	public String toString() {
		return new String(width + "*" + height);
	}

}
