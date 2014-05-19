package com.ignatieff.newton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Fractal {
	
	/**
	 * Array containing all the disposable colors for this generator.
	 * This list is Kelly's list of maximum contrast colors.
	 */
	private Color[] COLORS = { byteToColor(0xFFB300), //Vivid Yellow
		    		   byteToColor(0x803E75), //Strong Purple
		    		   byteToColor(0xFF6800), //Vivid Orange
		    		   byteToColor(0xA6BDD7), //Very Light Blue
		    		   byteToColor(0xC10020), //Vivid Red
		    		   byteToColor(0xCEA262), //Grayish Yellow
		    		   byteToColor(0x817066), //Medium Gray

		    		   //The following will not be good for people with defective color vision
		    		   byteToColor(0x007D34), //Vivid Green
		    		   byteToColor(0xF6768E), //Strong Purplish Pink
		    		   byteToColor(0x00538A), //Strong Blue
		    		   byteToColor(0xFF7A5C), //Strong Yellowish Pink
		    		   byteToColor(0x53377A), //Strong Violet
		    		   byteToColor(0xFF8E00), //Vivid Orange Yellow
		    		   byteToColor(0xB32851), //Strong Purplish Red
		    		   byteToColor(0xF4C800), //Vivid Greenish Yellow
		    		   byteToColor(0x7F180D), //Strong Reddish Brown
		    		   byteToColor(0x93AA00), //Vivid Yellowish Green
		    		   byteToColor(0x593315), //Deep Yellowish Brown
		    		   byteToColor(0xF13A13), //Vivid Reddish Orange
		    		   byteToColor(0x232C16)};//Dark Olive Green;
	
	private BufferedImage img;
	private int sizeH, sizeW, maxTime, bottom, increment;
	private double res, dr;
	private Map<Point, RootTime> roots;
	private ArrayList<Complex> usedColors;
	private ArrayList<Complex> points;
	
	/**
	 * A list of integers coprime to 20. Used to cause variation in the colors.
	 */
	private int[] increments = {1, 3, 7, 9, 11, 13, 17, 19};
	
	/**
	 * Converts an integer to a Color-object. Used to convert Kelly's list to Color-objects.
	 * @param color The color-integer 0xRRGGBB to convert.
	 * @return A Color-object representing the integer.
	 */
	private Color byteToColor(int color){
		return new Color(color);
	}
	
	/**
	 * Instantiates a new fractal with size = 800.
	 */
	public Fractal(){
		this(800);
	}
	
	/**
	 * Instantiates a new fractal with a specified size. Using this constructor will yield images of size (size, size).
	 * @param size The width and height in pixels of the images in the fractal.
	 */
	public Fractal(int size){
		this(size, 2);
	}
	
	/**
	 * Instantiates a new fractal with a specified size.
	 * @param sizeW The width in pixels of the fractal.
	 * @param sizeH The height in pixels of the fractal.
	 * @param resolution The resolution of the fractal. Will deal with complex numbers a + bi, where a, b are in [-res, res].
	 */
	public Fractal(int sizeW, int sizeH, double resolution){
		this.sizeW = sizeW;
		this.sizeH = sizeH;
		this.res  = resolution;
		this.dr = resolution * 2;
		bottom = (int) (Math.random()*22);
		increment = increments[(int)(Math.random() * increments.length)];
	}
	
	/**
	 * Instantiates a new fractal with a specified size and a specified resolution.
	 * @param size The height and width of the fractal (in pixels).
	 * @param resolution The resolution of the fractal. Will deal with complex numbers a + bi, where a, b are in [-res, res].
	 */
	public Fractal(int size, double resolution){
		this(size,size,resolution);
	}
	
	/**
	 * Will return the color associated with a specified root to the polynomial.
	 * @param root The root to map to a color.
	 * @return A color which is mapped to by the root.
	 */
	private Color getColorFromRoot(RootTime root){
		int i = 0;
		while(i < usedColors.size()){
			if(usedColors.get(i).equals(root.root)){
				Color c = COLORS[(bottom+i)%(COLORS.length)];
				double d = 0.2 + ((double)root.tries / (double)maxTime) * 0.8;
				Color q = new Color((int)(c.getRed() * d),
								 (int)(c.getGreen() * d),
								 (int)(c.getBlue() * d));
				return q;
			}
			i++;
		}
		return new Color(0,0,0);
	}
	
	/**
	 * Creates a new instance of the Fractal-object, generates a new, random fractal and saves it to the system.
	 * @param path The path of the output file (use .png).
	 * @param d The size of the image.
	 * @param complexity The complexity of the polynomial to use, read: terms of the polynomial.
	 * @throws IOException If the output file is read-only or otherwise unaccessible, this method will throw an IOException.
	 */
	public static void generateAndSaveFractal(String path, Dimension d, int complexity) throws IOException{
		File f = new File(path);
		Fractal fractal = new Fractal(d.width, d.height, 3);
		Polynomial p = Polynomial.random(complexity);
		BufferedImage img = fractal.generateFractal(p);
		ImageIO.write(writeToImage(img, p.toString()), "png", f);
	}
	
	/**
	 * 
	 * @param path Creates a new instance of the Fractal-object, generates a new, random fractal and saves it to the system.
	 * @param size The width and height of the image.
	 * @param complexity The complexity of the polynomial to use, read: terms of the polynomial.
	 * @throws IOException If the output file is read-only or otherwise unaccessible, this method will throw an IOException.
	 */
	public static void generateAndSaveFractal(String path, int size, int complexity) throws IOException{
		generateAndSaveFractal(path, new Dimension(size,size), complexity);
	}
	
	/**
	 * Writes text to a BufferedImage-object in the lower right corner.
	 * Used to write the name of the polynomial unto the image object.
	 * @param image The image to write on.
	 * @param text The text to write
	 * @return A new BufferedImage, which has text written unto it.
	 */
	private static BufferedImage writeToImage(BufferedImage image, String text){
		Graphics2D g = image.createGraphics();
		g.setFont(new Font("Sans-Serif", Font.BOLD, 17));
		FontMetrics fm = g.getFontMetrics(); 
		int w = fm.stringWidth(text) + 10;
		int h = fm.getHeight() + 3;
		g.setColor(Color.WHITE);
		g.fillRect(0, image.getHeight()-h, w, h);
		g.setColor(Color.BLACK);
		g.drawString(text, 5, image.getHeight() - 6);
		g.dispose();
		return image;
	}
	
	/**
	 * Generates a new fractal from a polynomial.
	 * @param p The polynomial to use to generate the fractal.
	 * @return A BufferedImage-object representing the newly generated fractal.
	 */
	public BufferedImage generateFractal(Polynomial p){
		
		img = new BufferedImage(sizeW, sizeH, BufferedImage.TYPE_INT_ARGB);
		usedColors = new ArrayList<Complex>();
		roots = new HashMap<Point, RootTime>();
		
		long a = System.currentTimeMillis();
		
		System.out.println("	Processing pixels...");
		
		for(int y=0; y<sizeH; y++){
			for(int x=0; x<sizeW; x++){
				processPixel(p, x, y);
			}
		}
		
		long dx = (System.currentTimeMillis() - a) / 1000;
		System.out.println("	Done processing pixels. Elapsed time: " + dx + " sec.\nColoring pixels...");
		a = System.currentTimeMillis();
		
		for(int y=0; y<sizeH; y++){
			for(int x=0; x<sizeW; x++){
				Color c = getColorFromRoot(
						roots.get(new Point(x,y)));
				img.setRGB(x, y, c.getRGB());
			}
		}
		
		dx = (System.currentTimeMillis() - a) / 1000;
		System.out.println("	Done coloring pixels. Elapsed time: " + dx + "sec.");
		
		return img;
		
	}
	
	/**
	 * Processes a single pixel of the image, based on a specific polynomial.
	 * Will use Newton's approximation to find the root which this specific point maps to.
	 * If it finds a new root, it will add it to the array of roots.
	 * @param p The polynomial to use to generate the fractals.
	 * @param x The x-coordinate (in pixels) of the pixel to process.
	 * @param y The y-coordinate (in pixels) of the pixel to process.
	 */
	private void processPixel(Polynomial p, int x, int y){
		Complex point = new Complex(getValueFromPixel(x, sizeW, 1),
									getValueFromPixel(y, sizeH, (double)sizeW / (double) sizeH)).update();
		
		Approximator a = new Approximator(p, point);
		
		RootTime rt = a.getRoot();
		
		if(rt.tries>maxTime)maxTime = rt.tries;
		
		if(!containsRoot(rt.root)){
			usedColors.add(rt.root);
		}
		
		roots.put(new Point(x,y), rt);
	}
	
	/**
	 * This method checks whether or not this Fractal-object already contains a specified root.
	 * @param root The root to check whether or not exists.
	 * @return Returns 'true' if it exists, and 'false' otherwise.
	 */
	private boolean containsRoot(Complex root){
		for(Complex c : usedColors){
			if(c.equals(root))return true;
		}
		return false;
	}
	
	/**
	 * Gets the unique complex number representing the pixel.
	 * @param z The x (or y) coordinate of the pixel in question.
	 * @param size The maximum coordinate of the pixel.
	 * @param mul The stretch-factor of the coordinate system.
	 * @return A double representing the complex (or real) part of the complex number.
	 */
	private double getValueFromPixel(int z, int size, double mul){
		return -(res/mul) + ((double)(dr/mul))*(((double)z) / size);
	}
}