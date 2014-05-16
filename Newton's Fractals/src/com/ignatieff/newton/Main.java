package com.ignatieff.newton;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {

		try {
			Fractal.generateAndSaveFractal("img.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
