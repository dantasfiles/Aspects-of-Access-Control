import java.util.*;

public abstract class Policy {

    public abstract TransRes translate(TreeSet<String> G) throws TransException;

}
