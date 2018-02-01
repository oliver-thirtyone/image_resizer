package test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.swing.ImageIcon;

import model.ImageSize;
import model.ResizableImage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testResizableImage {

	private ResizableImage image;
	private File newFile;
	private ImageSize targetSize;
	
	@Before
	public void setUp() throws Exception {
		image = new ResizableImage( new File("images/open.gif") );
		newFile = new File( "test/" + image.getFile().getName() );
		targetSize = new ImageSize(13, 37);
		
		new File("test").mkdirs();
	}

	@After
	public void tearDown() throws Exception {
		new File("test/test.gif").delete();
		new File("test").delete();
	}

	@Test
	public final void testRename() {
		image = new ResizableImage( new File("test/open.gif") );
		assertTrue( image.rename("test") );
		assertTrue( new File("test/test.gif").isFile() );		
	}

	@Test
	public final void testResize() {
		assertTrue( image.resize(newFile, targetSize) );
		
		ImageIcon resizedImage = new ImageIcon(newFile.getAbsolutePath());
		assertTrue(resizedImage.getIconWidth() == targetSize.getWidth());
		assertTrue(resizedImage.getIconHeight() == targetSize.getHeight());
	}

}
