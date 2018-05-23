public class TransException extends Exception {

    Policy p;
    public TransException (Policy p) {
	//System.out.println("test" + p.toString());
	this.p = p;
    }
    
    public String toString() {
	return p.toString();
    }

}
