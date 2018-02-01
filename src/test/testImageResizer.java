package test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;

import model.ImageResizer;
import model.ImageSize;
import model.ResizableImage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testImageResizer implements Observer {
	
	private String currentImage;
	private ImageResizer imageResizer;
	private ArrayList<ResizableImage> images;
	private ArrayList<ResizableImage> resizedImages;
	private ImageSize targetSize;
	
	@Before
	public void setUp() throws Exception {		
		imageResizer = ImageResizer.getImageResizer(this);
		imageResizer.setOutputDir("test");
		imageResizer.setOverwriteType(ImageResizer.OVERWRITE_IMAGES);
		imageResizer.setRenameName("test");
		imageResizer.setRenameIndex(1);
		
		currentImage = "";
		targetSize = new ImageSize(100, 100);
		
		images = new ArrayList<ResizableImage>();
		images.add(new ResizableImage(new File("images/open.gif")));
		images.add(new ResizableImage(new File("images/open.gif")));
		images.add(new ResizableImage(new File("images/open.gif")));
		
		resizedImages = new ArrayList<ResizableImage>();
		
		new File(imageResizer.getOutputDir()).mkdirs();
	}

	@After
	public void tearDown() throws Exception {	
		for (ResizableImage image : resizedImages) {
			image.getFile().delete();
		}
		new File(imageResizer.getOutputDir()).delete();
	}

	@Test
	public final void testResizeImages() {
		assertTrue( imageResizer.resizeImages(images, ImageResizer.DEFINED_SIZE, targetSize, true) );

		for (ResizableImage image : resizedImages) {
			ImageIcon resizedImage = new ImageIcon(image.getFile().getAbsolutePath());
			assertTrue(resizedImage.getIconWidth() == targetSize.getWidth());
			assertTrue(resizedImage.getIconHeight() == targetSize.getHeight());
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.currentImage = imageResizer.getCurrentImage();
		this.resizedImages.add(new ResizableImage(new File(currentImage)));
	}	
}
