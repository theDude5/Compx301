public class REcompile {
    private class State {
        int n1, n2, st;
        char ch;
        public State(char ch, int n1) {
            this.ch = ch;
            this.n1 = n1;
            n2 = n1;
            this.st = state++;
        }
        public void print(){ System.out.println(st+"\t"+ch+"\t"+n1+"\t"+n2); }
    }

    char[] regex;
    State[] states;
    int i, s, state = 0;
    public REcompile(String regex){
        this.regex = ('\0'+regex.substring(1, regex.length()-1)).toCharArray();
        states= new State[this.regex.length];
        //parse();
    }

    public void expression(){
        term();
        if (i < regex.length && !"*?|)".contains(regex[i]+"")) { expression(); }
    }

    public void term(){
        int r = factor();
        if (i >= regex.length || !"*?|".contains(regex[i]+"")) { return; }        
        if (states[state-1].ch != '\0') { states[state] = new State('\0', state+1); }
        states[state-1].n2 = r;
        //r==s
        if(states[r-1].n2 == states[r-1].n1) {states[r-1].n2=state-1;}
        states[r-1].n1=state-1;
        if (regex[i] == '*') {
            states[s].n1 = state;
            states[s].n2 = r < s && states[r+1].ch == '\0' ? r+1 : r;
            i++;
        }
        else if (regex[i] == '?') {
            if (states[s].n2 == states[s].n1) { states[s].n2 = state; }
            states[s].n1 = state; i++;
            while (r < state) { 
                if (states[r].n1 == state-1) { states[r].n1++; }
                if (states[r].n2 == state-1) { states[r].n2++; }
                r++;
            }
        }
        else if (regex[i] == '|') {
            int ref = state-1, t = s; i++;
            term();
            if (states[t].n2 == states[t].n1) { states[t].n2 = state; }
            states[t].n1 = state;
            while (r < ref) { if (states[r].n2 == ref) { states[r].n2 = state; } r++; }
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
                int r = state; i++;
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

    public State[] parse() {
        i = 0;
        expression();
        if (i != regex.length){ throw new Error("Illegal Expression"); }
        return states;
    }

    public void print() {
        System.out.println("state\tch\tn1\tn2\n---------------------------");
        for (State c: states) { if (c != null) { c.print(); } }
    }

    public static void main(String[] args) {
        if (args.length == 0 || !args[0].startsWith("\"") || !args[0].endsWith("\"")) { 
            System.out.println("Usage: java REcompile \\\" <expression> \\\"");
            args = new String[] {"\""+"a|(jm?b*)|c*"+"\""};
            // return;
        }
        REcompile RE = new REcompile(args[0]);
        try { RE.parse(); RE.print(); } catch (Error e) { System.err.println(e); }
    }
}
