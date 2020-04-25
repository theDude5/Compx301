public class REcompile {
    private class State {
        int n1, n2, st;
        char ch;
        public State(char ch, int n2) {
            this.ch = ch;
            this.n2 = n2;
            n1 = n2;
            this.st = state++;
        }
    }

    char[] regex;
    State[] states;
    int i, s, state = 0;
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
        if (i >= regex.length) { return; }
        if (regex[i] == '*') { states[state-1].n2 = r; i++; }
        else if (regex[i] == '?') {
            if (states[state-1].ch != '\0') { r--; }
            states[r].n2 = state;
            i++; 
        }
        else if (regex[i] == '|') {
            if (states[state-1].ch != '\0') { states[state] = new State('\0', state+1); }
            states[state-1].n2 = r;
            if (states[r-1].n1 == states[r-1].n2) { states[r-1].n2 = state-1; }
            states[r-1].n1 = state-1;
            r = s; i++;
            term();
            if (states[r].n1 == states[r].n2) { states[r].n2 = state+1; }
            states[r].n1 = state+1;
        }
    }

    /**
     * @return first index of last expression
     */
    public int factor(){
        if ("*?|".contains(regex[i]+"")) { throw new Error("Misplaced operator"); }
        switch (regex[i]) {
            case '\\':
                s = state;
                states[state] = new State(regex[i+1], state+1);
                i+=2;
                break;
            case '(':
                states[state] = new State('\0', state+1);
                int r = state-1; i++;
                expression();
                if (i >= regex.length || regex[i] != ')') { throw new Error("Missing ')'"); }
                i++;
                return r;
            case ')': break;
            // case '.': s = state; states[state] = new State('.', state+1); i++; break;
            default: s = state; states[state] = new State(regex[i], state+1); i++;
        }
        return state-1;
    }

    public void parse() {
        i = 0;
        expression();
        if (i != regex.length){ throw new Error("Illegal Expression"); }
        print();
    }

    public void print() {
        System.out.println("state\tch\tn1\tn2\n---------------------------");
        for (State c: states) { if (c != null) { System.out.println(c.st+"\t"+c.ch+"\t"+c.n1+"\t"+c.n2); }}
    }

    public static void main(String[] args) {
        if (args.length == 0 || !args[0].startsWith("\"") || !args[0].endsWith("\"")) { 
            System.out.println("Usage: java REcompile \\\" <expression> \\\"");
            args = new String[] {"\""+".(\\?(c?)|((mb)?)|Cm*)?"+"\""};
            // return;
        }
        try { REcompile RE = new REcompile(args[0]); } catch (Error e) { System.err.println(e); }
    }
}
