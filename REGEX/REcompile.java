public class REcompile {
    private class State {
        int n1, n2;
        char ch;
        public State(char ch, int n2) {
            this.ch = ch;
            this.n2 = n2;
            n1 = n2;
        }
    }

    char[] regex;
    State[] states;
    int i, s;
    public REcompile(String regex){
        this.regex = (regex.substring(1, regex.length()-1)).toCharArray();
        states= new State[this.regex.length];
        parse();
    }

    public void expression(){
        term();
        if (i < regex.length && !"*?|)".contains(regex[i]+"")) { expression(); }
    }

    public void term() {
        int r = factor();
        if (regex[i] == '*') {
            states[s].n1 = ++i; 
            states[s].n2 = r;
        }
        else if (regex[i] == '?') {
            states[s].n1++;
            states[r].n2 = ++i;
        }
        else if (regex[i] == '|') { 
            int temp = i++;
            term();
        }
    }

    public int factor(){
        if ("*?|".contains(regex[i]+"")) { throw new Error("Misplaced operator"); }
        switch (regex[i]) {
            case '\\':
                if (states[s].n1 == states[s].n2) { states[s].n2++; }
                states[s].n1++; 
                s = ++i;
                states[i] = new State(regex[i], ++i);
                break;
            case '(':
                if (states[s].n1 == states[s].n2) { states[s].n2++; }
                states[s].n1 = i+1;
                int r = ++i;
                expression();
                if (i >= regex.length || regex[i] != ')') { throw new Error("Missing ')'"); }      
                if (states[s].n1 == states[s].n2) { states[s].n2++; }
                states[s].n1 = ++i;
                while (states[r] == null && r < i) { r++; }
                return r;
            case ')': break;
            case '.': s = i; states[i] = new State('.', ++i); break;
            default: s = i; states[i] = new State(regex[i], ++i);
        }
        return i-1;
    }

    public void parse() {
        i = 0;
        expression();
        if (i != regex.length){ throw new Error("Illegal Expression"); }
        print();
    }
    public void print() {
        System.out.println("ch\tn1\tn2");
        System.out.println("------------------");
        for (State c: states) { if (c != null) { System.out.println(c.ch+"\t"+c.n1+"\t"+c.n2); }}
    }
    public static void main(String[] args) {
        if (args.length == 0 || !args[0].startsWith("\"") || !args[0].endsWith("\"")) { 
            System.out.println("Usage: java REcompile \\\" <expression> \\\"");
            args = new String[] {"\""+".a*(()a)*\\?.|a*"+"\""};
            // return;
        }
        try { 
            REcompile RE = new REcompile(args[0]);
        } catch (Error e) { System.err.println(e); }
    }
}
