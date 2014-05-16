package com.ignatieff.newton;

public class RootTime {
	public Complex root;
	public int tries;
	
	public RootTime(Complex root, int tries){
		this.root = root.update();
		this.tries = tries;
	}
	
	@Override
	public String toString(){
		return "";
	}
	public boolean isEqual(RootTime a){
		return (a.root.equals(root));
	}
	public boolean isEqual(Complex c){
		return root.equals(c);
	}
}
