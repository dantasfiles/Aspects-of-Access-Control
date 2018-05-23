import java.util.*;

public class PolLet extends Policy {
    String var;
    Policy p1;
    Policy p2;
    /* counter? */
    

    public PolLet (String var, Policy p1, Policy p2) {
	this.var = var;
	this.p1 = p1;
	this.p2 = p2;
    }

    public String toString() {
	return "(let " + var + "=" + p1.toString() + " in " + p2.toString() + " end)";
    }

    public TransRes translate (TreeSet<String> G) throws TransException {
	TransRes res1 = p1.translate(G);
	String exp1 = res1.exp;
	ArrayList<String> rs1 = res1.rs;
	ArrayList<String> files1 = res1.files;
	ArrayList<String> asps1 = res1.asps;
	TreeSet<String> newG = (TreeSet<String>) G.clone();
	newG.add(var);
 	TransRes res2 = p2.translate(newG);
	String exp2 = res2.exp;
	ArrayList<String> rs2 = res2.rs;
	ArrayList<String> files2 = res2.files;
	ArrayList<String> asps2 = res2.asps;
	String result = "";
	result += "public aspect " + var + "asp extends Policy {\n";
	result += "  declare precedence : ";
	for (String r : rs1) {
	    result += r + "asp, ";
	}
	result += var + "asp;\n";
	result += "  public pointcut pc () : ";
	boolean first = true;
	for (String r : rs1) {
	    if (!first) {
		result += " || "; 
	    }
	    result += r + "asp.pc()";
	    first = false;
	}
	result += ";\n";
 	for (String r : rs1) {
	    result += "  Auth " + r + "field = Auth.NA;\n";
	    result += "  before (Auth auth)\n";
	    result += "    : call(void " + r + "asp.result(Auth))\n";
	    result += "      && args(auth) {\n";
	    result += "    " + r + "field = auth;\n";
	    result += "  }\n";
	    result += "  before () : pc () {\n";
	    result += "    result(" + exp1 + ");\n";
	    result += "    " + r + "field = Auth.NA;\n";
	    result += "  }\n";
	    result += "  after () throwing () : pc () {\n";
	    result += "    " + r + "field = Auth.NA;\n";
	    result += "  }\n";
	}
	result += "  }\n";
	ArrayList<String> files = new ArrayList<String>();
	files.addAll(files1);
	files.add(var+"asp.java");
	files.addAll(files2);
	ArrayList<String> asps = new ArrayList<String>();
	asps.addAll(asps1);
	asps.add(result);
	asps.addAll(asps2);

 	return new TransRes(exp2, 
			    rs2, 
			    files, asps);
    }

}
