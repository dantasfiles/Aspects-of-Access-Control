import java.util.*;

public class PolNot extends Policy {
    public Policy p;

    public PolNot (Policy p) {
	this.p = p;
    }

    public String toString() {
	return "(~" + p.toString() + ")";
    }

    public TransRes translate (TreeSet<String> G) throws TransException {
	TransRes res = p.translate(G);
	String exp = res.exp;
	ArrayList<String> rs = res.rs;
	ArrayList<String> files = res.files;
	ArrayList<String> asps = res.asps;
	return new TransRes(exp + ".negate()", rs, files, asps);
    }

}
