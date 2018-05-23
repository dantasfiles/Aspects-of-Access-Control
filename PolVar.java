import java.util.*;

public class PolVar extends Policy {
    String r;

    public PolVar (String r) {
	this.r = r;
    }

    public String toString() {
	return "(#" + r + ")";
    }

    public TransRes translate (TreeSet<String> G) throws TransException  {
	if (!G.contains(r)) {
	    throw new TransException(this);
	} 
	ArrayList<String> rs = new ArrayList<String>();
	rs.add(r);
	
	return new TransRes(r + "field", rs, new ArrayList<String>(), new ArrayList<String>());
    }
    
}
