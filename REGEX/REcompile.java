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
        this.regex = (regex.substring(1, regex.length()-1)).toCharArray();
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
        if (r > 0){
            if (states[r-1].n2 == states[r-1].n1) { states[r-1].n2 = state-1; }
            if (states[r-1].ch != '\0' && states[r-1].n2 == r){ states[r-1].n1 = state-1; }
        }
        states[state-1].n2 =  r; 
        if (regex[i] == '*') {
            states[s].n1 = state-1; states[s].n2 = r; i++;
            while (r < state) { 
                if (states[r].n1 > state-1) { states[r].n1 = state-1; }
                if (states[r].n2 > state-1) { states[r].n2 = state-1; }
                r++;
            }
        }
        else if (regex[i] == '?') {
            if (states[s].n2 == states[s].n1) { states[s].n2 = state; }
            states[s].n1 = state; i++;
            while (r < state) { 
                if (states[r].n1 == state-1) { states[r].n1 = state; }
                if (states[r].n2 == state-1) { states[r].n2 = state; }
                r++;
            }
        }
        else if (regex[i] == '|') { 
            states[r-1].n1 = state-1;
            if (states[r].n2 == state-1) { states[r].n2 = states[r].n1; }
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

    public State[] parse() {
        i = 0;
        expression();
        if (i != regex.length){ throw new Error("Illegal Expression"); }
        cleanup();
        return states;
    }

    public void print() {
        System.out.println("state\tch\tn1\tn2\n---------------------------");
        for (State c: states) { if (c != null) { c.print(); } }
        System.out.println("Start State = "+(states[0]!=null? 0: state-1));
    }

    public void cleanup(){
        for (int i = 0; i < state; i++) {
            if (states[i].ch != '\0' || states[i].n2 != states[i].n1 || states[i].n1 != i+1){ continue; }
            for (int j = 0; j < state; j++) {
                if (states[j] != null) {
                    if (states[j].n1 == i) { states[j].n1++; }
                    if (states[j].n2 == i) { states[j].n2++; }
                }
            }
            states[i] = null;
        }
        if (states[0]!=null) { states[state-1]=null; }
    }

    public static void main(String[] args) {
        if (args.length == 0 || !args[0].startsWith("\"") || !args[0].endsWith("\"")) { 
            System.out.println("Usage: java REcompile \\\" <expression> \\\"");
            args = new String[] {"\""+".(.(\\?(c?))|((m?b)*)|(Cm?)|n)?"+"\""};
            // return;
        }
        REcompile RE = new REcompile(args[0]);
        try { RE.parse(); RE.print(); } catch (Error e) { System.err.println(e); }
    }
}
