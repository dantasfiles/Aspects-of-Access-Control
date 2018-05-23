import java.io.*;
import java.util.*;

public class Translate {

    //	static StreamTokenizer policystream = null;
	
    public static void main (String [] args) throws IOException, FileNotFoundException {
	String policyfilename = args[0];
	FileReader policyfile = null;
	    policyfile = new FileReader(policyfilename);
	StreamTokenizer pstream = new StreamTokenizer(policyfile);
	Parser parse = new Parser (pstream);
	Policy p = null;
	try {
	    p = parse.parse();
	} catch (ParseException e) {
	    System.out.println(e.s);
	    e.printStackTrace();
	    System.exit(0);
	}
	TransRes res = null;
	try {
	    res = translate(p);
	} catch (TransException e) {
	    System.out.println(e.toString());
	    e.printStackTrace();
	    System.exit(0);
	}
	ArrayList<String> files = res.files;
	ArrayList<String> asps = res.asps;
	for (int i = 0; i < files.size(); i++) {
	    FileWriter fw = new FileWriter("test/" + files.get(i));
	    fw.write(asps.get(i));
	    fw.close();
	}
     
	    
    }

    
    
    public static TransRes translate (Policy p) throws TransException {
	TransRes res = p.translate(new TreeSet<String>());
	String exp = res.exp;
	ArrayList<String> rs = res.rs;
	ArrayList<String> files = res.files;
	ArrayList<String> asps = res.asps;

	String result = "";
	result += "public aspect Top extends Policy {\n";
	result += "  declare precedence : ";
	for (String r : rs) {
	    result += r + "asp, ";
	}
	result += "Top;\n";
	result += "  public pointcut pc () : ";
	boolean first = true;
	for (String r : rs) {
	    if (!first) {
		result += " || "; 
	    }
	    result += r + "asp.pc()";
	    first = false;
	}
	result += ";\n";
  	result += "  public void result (Auth result) {\n";
	result += "    switch (result) {\n";
	result += "      case DENY :\n";
	result += "      case CONFLICT :\n";
	result += "        System.exit(-1);\n";
	result += "    }\n";
        result += "  }\n";
	for (String r : rs) {
	    result += "  Auth " + r + "field = Auth.NA;\n";
	    result += "  before (Auth auth)\n";
	    result += "    : call(void " + r + "asp.result(Auth))\n";
	    result += "      && args(auth) {\n";
	    result += "    " + r + "field = auth;\n";
	    result += "  }\n";
	    result += "  before () : pc () {\n";
	    result += "    result(" + exp + ");\n";
	    result += "    " + r + "field = Auth.NA;\n";
	    result += "  }\n";
	    result += "  after () throwing () : pc () {\n";
	    result += "    " + r + "field = Auth.NA;\n";
	    result += "  }\n";
	}
	result += "}\n";
	ArrayList<String> newfiles = new ArrayList<String>();
	newfiles.addAll(files);
	newfiles.add("Top.java");
	ArrayList<String> newasps = new ArrayList<String>();
	newasps.addAll(asps);
	newasps.add(result);
	return new TransRes(null, null, newfiles, newasps);
    }
}





