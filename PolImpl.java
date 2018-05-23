import java.util.*;

public class PolImpl extends Policy {
    public Policy p1;
    public Policy p2;

    public PolImpl (Policy p1, Policy p2) {
	this.p1 = p1;
	this.p2 = p2;
    }
    
    public TransRes translate (TreeSet<String> G) throws TransException {
	TransRes res1 = p1.translate(G);
	String exp1 = res1.exp;
	ArrayList<String> rs1 = res1.rs;
	ArrayList<String> files1 = res1.files;
	ArrayList<String> asps1 = res1.asps;
 
	TransRes res2 = p2.translate(G);
	String exp2 = res2.exp;
	ArrayList<String> rs2 = res2.rs;
	ArrayList<String> files2 = res2.files;
	ArrayList<String> asps2 = res2.asps;

	ArrayList<String> newrs = (ArrayList<String>) rs1.clone();
	newrs.addAll(rs2);
	ArrayList<String> files = (ArrayList<String>) files1.clone();
	files.addAll(files2);
	ArrayList<String> asps = (ArrayList<String>) asps1.clone();
	asps.addAll(asps2);

	return new TransRes(exp1 + ".impl(" + exp2 + ")", 
			    newrs, 
			    files, asps);
    }

    public String toString() {
	return "(" + p1.toString() + "^" + p2.toString() + ")";
    }

}
