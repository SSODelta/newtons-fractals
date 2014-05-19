package com.ignatieff.newton;

import java.awt.Dimension;
import java.io.IOException;


public class Main {

	public static void main(String[] args) {
		for(int i=0; i<100; i++){
			try {
				System.out.println("Generating image " + i +"/100 ...");
				Fractal.generateAndSaveFractal("output"+i+".png", new Dimension(1280, 720), 3+(int)(Math.random()*3));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
