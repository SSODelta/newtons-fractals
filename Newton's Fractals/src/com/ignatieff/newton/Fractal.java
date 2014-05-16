package com.ignatieff.newton;

import java.awt.Color;
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
import javax.swing.colorchooser.ColorSelectionModel;

public class Fractal {
	
	Color[] COLORS = { byteToColor(0xFFB300), //Vivid Yellow
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
	
	BufferedImage img;
	int size, maxTime, bottom;
	double res, dr;
	Map<Point, RootTime> roots;
	ArrayList<Complex> usedColors;
	ArrayList<Complex> points;
	
	public Color byteToColor(int color){
		return new Color(color);
	}
	
	public Fractal(){
		this(800);
	}
	
	public Fractal(int size){
		this(size, 2);
	}
	
	public Fractal(int size, double resolution){
		this.size = size;
		this.res  = resolution;
		this.dr = resolution * 2;
		bottom = (int) (Math.random()*22);
	}
	
	public Color getColorFromRoot(RootTime root){
		int i = 0;
		while(i < usedColors.size()){
			//System.out.println("	Iterating i (i="+i+")");
			if(usedColors.get(i).equals(root.root)){
				Color c = COLORS[(bottom+i)%(COLORS.length)];
				double d = 0.2 + ((double)root.tries / (double)maxTime) * 0.8;
				Color q = new Color((int)(c.getRed() * d),
								 (int)(c.getGreen() * d),
								 (int)(c.getBlue() * d));
				//System.out.println("q = " + q);
				return q;
			}
			i++;
		}
		return new Color(0,0,0);
	}
	
	public static void generateAndSaveFractal(String path, int size, int complexity) throws IOException{
		File f = new File(path);
		Fractal fractal = new Fractal(size);
		Polynomial p = Polynomial.random(complexity);
		BufferedImage img = fractal.generateFractal(p);
		ImageIO.write(writeToImage(img, p.toString()), "png", f);
	}
	
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
	
	public BufferedImage generateFractal(Polynomial p){
		
		//System.out.println("Polynomial = " + p.toString());
		
		img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		usedColors = new ArrayList<Complex>();
		roots = new HashMap<Point, RootTime>();
		
		for(int y=0; y<size; y++){
			for(int x=0; x<size; x++){
				processPixel(p, x, y);
			}
		}
		
		/*System.out.println("Roots for '" + p.toString() + "' are :");
		for(Complex rt : usedColors){
			System.out.println(rt.toString());
		}*/
		
		for(int y=0; y<size; y++){
			for(int x=0; x<size; x++){
				Color c = getColorFromRoot(
						roots.get(new Point(x,y)));
				img.setRGB(x, y, c.getRGB());
			}
		}
		
		return img;
		
	}
	
	public void processPixel(Polynomial p, int x, int y){
		Complex point = new Complex(getValueFromPixel(x),
									getValueFromPixel(y)).update();
		
		Approximator a = new Approximator(p, point);
		
		RootTime rt = a.getRoot();
		
		if(rt.tries>maxTime)maxTime = rt.tries;
		
		if(!containsRoot(rt.root)){
			usedColors.add(rt.root);
		}
		
		roots.put(new Point(x,y), rt);
	}
	
	public boolean containsRoot(Complex root){
		for(Complex c : usedColors){
			if(c.equals(root))return true;
		}
		return false;
	}
	
	public double getValueFromPixel(int z){
		return -res + ((double)dr)*(((double)z) / size);
	}
}
