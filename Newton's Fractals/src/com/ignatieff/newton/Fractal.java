package com.ignatieff.newton;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

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
	int size;
	double res, dr;
	Map<Point, RootTime> roots;
	ArrayList<Complex> usedColors, points;
	
	public Color byteToColor(int color){
		return new Color(color);
	}
	
	public Fractal(){
		this(800);
	}
	
	public Fractal(int size){
		this(size, 0.5);
	}
	
	public Fractal(int size, double resolution){
		this.size = size;
		this.res  = resolution;
		this.dr = resolution * 2;
	}
	
	public Color getColorFromRoot(Complex root){
		int i = 0;
		while(i < usedColors.size()){
			//System.out.println("	Iterating i (i="+i+")");
			if(usedColors.get(i).equals(root))
				return COLORS[i];
			i++;
		}
		return new Color(0,0,0);
	}
	
	public static void generateAndSaveFractal(String path) throws IOException{
		File f = new File(path);
		Fractal fractal = new Fractal(800);
		BufferedImage img = fractal.generateFractal(Polynomial.random(3));
		ImageIO.write(img, "png", f);
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
		
		System.out.println("The polynomial '"+p.toString()+"' has " + usedColors.size() + " roots.");
		
		for(int y=0; y<size; y++){
			for(int x=0; x<size; x++){
				Color c = getColorFromRoot(
						roots.get(new Point(x,y)).root.update());
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
