package fhscqs;

public class VarContainer<E> {	//Class for storing an item for interfacing with inner-classes
	private E var;
	
	public VarContainer(E e) { this.var = e; }
	public E getVar() {return var;}
	public void setVar(E e) {this.var = e;}
}
