package com.ignatieff.newton;

public class RootTime {
	public Complex root;
	public int tries;
	
	/**
	 * Instantiates a new RootTime object.
	 * 
	 * This data-type is used to transfer both the root which is mapped to by a complex number, but also how long it took to approximate.
	 * 
	 * @param root The root which is mapped to by an initial guess.
	 * @param tries The number of tries used to approximate the root.
	 */
	public RootTime(Complex root, int tries){
		this.root = root.update();
		this.tries = tries;
	}
	
	@Override
	public String toString(){
		return root.toString() + " (" + tries + ")";
	}
	
	/**
	 * Will check if two RootTime objects contain the same root.
	 * @param a The RootTime object to check equality on.
	 * @return Will return 'true' if the roots are equal.
	 */
	public boolean isEqual(RootTime a){
		return (a.root.equals(root));
	}
	
	/**
	 * Will check if this RootTime-object has the same root as another.
	 * @param a The Complex object to check equality on.
	 * @return Will return 'true' if the roots are equal.
	 */
	public boolean isEqual(Complex c){
		return root.equals(c);
	}
}
