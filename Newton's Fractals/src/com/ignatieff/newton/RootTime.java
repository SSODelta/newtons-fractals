package com.ignatieff.newton;

public class RootTime {
	public Complex root;
	public int tries;
	
	public RootTime(Complex root, int tries){
		this.root = root.update();
		this.tries = tries;
	}
}
