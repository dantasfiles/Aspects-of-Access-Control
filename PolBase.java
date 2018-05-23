import java.util.*;
import java.lang.reflect.*;


public class PolBase extends Policy {
    static int counter = 0;

    public enum Auth { GRANT, DENY }

    public Auth auth;
    public String C1;
    public String x;
    public String C2;
    public String y;
    public String C3;
    public String m;
    public String [] C4s;
    public String [] zs;
    public String e;

    public PolBase (String auth, String C1, String x, String C2, String y, String C3, String m, Object[] C4s, Object [] zs, String e) {
	if (auth.equals("grant")) {
	    this.auth = Auth.GRANT;
	} else {
	    this.auth = Auth.DENY;
	}
	this.C1 = C1;
	this.x = x;
	this.C2 = C2;
	this.y = y;
	this.C3 = C3;
	this.m = m;
	this.C4s = new String [C4s.length];
	this.zs = new String [zs.length];
	for (int i = 0; i < C4s.length; i++) {
	    this.C4s[i] = (String) C4s[i];
	    this.zs[i] = (String) zs[i];
	}
	this.e = e;
    }

    private static int getNext () {
	return counter++;
    }

    public String transAuth (Auth a) {
	switch (a) {
	case GRANT : return "Auth.GRANT";
	case DENY : return "Auth.DENY";
	}
	return "";
    }
    
    public TransRes translate (TreeSet<String> G) throws TransException {
	/* VERIFICATION */
	try {
	    Class.forName(C1);
	} catch (ClassNotFoundException e) {
	    throw new TransException(this);
	}
	Class class2;
       	try {
	    class2 = Class.forName(C2);
	} catch (ClassNotFoundException e) {
	    throw new TransException(this);
	}
	Class[] class4 = new Class[C4s.length];
	for (int i = 0; i < C4s.length; i++) {
	    try {
		class4[i] = Class.forName(C4s[i]);
	    } catch (ClassNotFoundException e) {
		throw new TransException(this);
	    }
	}
	Method met;
	//	for (Method m : class2.getMethods()) {  
	//	    System.out.println(m.toString());
	//}
	/*
	try {
	    met = class2.getMethod(m, class4);
	} catch (NoSuchMethodException e) {
	    throw new TransException(this);
	}
	Class class3;
	try {
	    class3 = Class.forName(C3);
	} catch (ClassNotFoundException e) {
	    throw new TransException(this);
	}
	if (!met.getReturnType().equals(class3)) {
	    throw new TransException(this);
	    }*/
	String result = "";
	String newvar = "r" + PolBase.getNext();
	result += "public aspect " + newvar + "asp extends Policy {\n";
	result += "  pointcut locpc (" + C1 + " " + x + ", " + C2+ " " + y;
	for (int i = 0; i < C4s.length; i++) {
	    result += ", " + C4s[i] + " " + zs[i];
	}
	result += ")\n";
	result += "    : call (" + C3 + " " + C2 + "+." + m + "(";
	boolean first = true;
	for (String C4 : C4s) {
	    if (!first) {
		result += ", ";
	    }
	    result += C4;
	    first = false;
	}
	result += "))\n";
        result += "      && this(" + C1 + ")\n";
	result += "      && this(" + x + ")\n"; 
        result += "      && target(" + y + ")\n";
        result += "      && args (";
	first = true;
	for (String z : zs) {
	    if (!first) {
		result += ", ";
	    }
	    result += z;
	    first = false;
	}
	result += ");\n";
	result += "  public pointcut pc () : locpc (*, *";
	for (String z : zs) {
	    result += ", *";
	}
	result += ");\n";
	result += "  before (" + C1 + " " + x +", " + C2 + " " + y;
	for (int i = 0; i < C4s.length; i++) {
	    result += ", " + C4s[i] + " " + x;
	}
	result += ") \n";
	result += "    : locpc (" + x + ", " + y;
	for (String z : zs) {
	    result += ", " + z;
	}
	result += ") {\n";
	result += "    if (" + e + ") {\n";
	result += "      result(" + transAuth(auth) + ");\n";
	result += "    }\n";
	result += "  }\n";
	result += "}\n";
	ArrayList<String> rs = new ArrayList<String>();
	rs.add(newvar);
	ArrayList<String> files = new ArrayList<String>();
	files.add(newvar+"asp.java");
	ArrayList<String> asps = new ArrayList<String>();
	asps.add(result);
	//System.out.println(result);
	return new TransRes (newvar+"field", rs, files, asps);
    }
    
    public String toString() {
	return "(auth " + "(" + C1 + " " + x + "," + C2 + " " + y + "," + C3 + " " + m + ") if " + e + " end)";
    }

}
