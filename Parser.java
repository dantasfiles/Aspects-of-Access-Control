import java.io.*;
import java.util.*;

public class Parser {

    StreamTokenizer pstream;

    public Parser (StreamTokenizer pstream) {
	this.pstream = pstream;
    }


     public String nextToken() throws ParseException {
	int nextToken;
	try {
	    nextToken = pstream.nextToken();
	} catch (IOException e) {
	    throw new ParseException("Bad IO");
	}
	if (nextToken != StreamTokenizer.TT_WORD) {
	    return Character.toString((char) nextToken);
	}
	return pstream.sval;
    }
    
   public void parseRPAREN () throws ParseException  {
	String s = nextToken();
	if (!s.equals(")")) {
	    throw new ParseException(s);
	}
    }

    public void parseCOMMA () throws ParseException {
	String s = nextToken();
	if (!s.equals(",")) {
	    throw new ParseException(s);
	}
    }

    public void parseLPAREN () throws ParseException {
	String s = nextToken();
	if (!s.equals("(")) {
	    throw new ParseException(s);
	}
    }

    public void parseEND () throws ParseException {
	String s = nextToken();
	if (!s.equals("end")) {
	    throw new ParseException(s);
	}
    }

    public void parseIN () throws ParseException {
	String s = nextToken();
	if (!s.equals("in")) {
	    throw new ParseException(s);
	}
    }

    public void parseIF () throws ParseException {
	String s = nextToken();
	if (!s.equals("if")) {
	    throw new ParseException(s);
	}
    }

    public void parseEQ () throws ParseException {
	String s = nextToken();
	if (!s.equals("=")) {
	    throw new ParseException(s);
	}
    }
	

    public Policy parse () throws ParseException {
	String curr = nextToken();
	Policy res;
	if (curr.equals("let")) {
	    String var = nextToken();
	    parseEQ();
	    Policy p1 = parse();
	    parseIN();
	    Policy p2 = parse();
	    parseEND();
	    res = new PolLet(var, p1, p2);
	} else if (curr.equals("~")) {
	    res = new PolNot(parse());
	} else if (curr.equals("^")) {
	    res = new PolAnd(parse(), parse());
	} else if (curr.equals(">")) {
	    res = new PolImpl(parse(), parse());
	} else if (curr.equals("grant") || curr.equals("deny")) {
	    parseLPAREN();
	    String C1 = nextToken();
	    String x = nextToken();
	    parseCOMMA();
	    String C2 = nextToken();
	    String y = nextToken();
	    parseCOMMA();
	    String C3 = nextToken();
	    String m = nextToken();
	    parseLPAREN();
	    String next = nextToken();
	    ArrayList<String> C4s = new ArrayList<String>();
	    ArrayList<String> zs = new ArrayList<String>();
	    while (!next.equals(")")) {
		C4s.add(nextToken());
		zs.add(nextToken());
		next = nextToken();
		if (next.equals(",")) {
		    next = nextToken();
		}
	    }
	    parseRPAREN();
	    parseIF();
	    
	    String exp = "";
	    String end = nextToken();
	    while (!end.equals("end")) {
		exp += " " + end;
		end = nextToken();
	    }
	    //  parseEND();
	    res = new PolBase(curr, C1, x, C2, y, C3, m, C4s.toArray(), zs.toArray(), exp);
	} else {
	    res = new PolVar (curr);
	}
	return res;
    }

}
