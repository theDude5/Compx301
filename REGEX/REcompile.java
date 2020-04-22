public class REcompile {
    private class State {
        int n1, n2;
        char ch;
        public State(int n1, int n2) {
            ch = '\0';
            this.n1 = n1;
            this.n2 = n2;
        }
        public State(char ch, int n1, int n2) {
            this.ch = ch;
            this.n1 = n1;
            this.n2 = n2;
        }    
    }
    char[] regex;
    State[] states;
    int i, s;
    public REcompile(String regex){
        this.regex = (regex.substring(1, regex.length()-1)).toCharArray();
        // states= new State[this.regex.length+1];
        parse();
    }

    public void expression(){
        term();
        if (i < regex.length && !"*?|)".contains(regex[i]+"")) { expression(); }
    }

    public void term() {
        factor();
        if (regex[i] == '*') { i++; }
        else if (regex[i] == '?') { i++; }
        else if (regex[i] =='|') { i++; term(); }
    }

    public void factor(){
        if ("*?|".contains(regex[i]+"")) { throw new Error("Misplaced opereator"); }
        switch (regex[i]) {
            case '\\': i+=2; break;
            case '.': i++; break;
            case '(':
                i++;
                expression();
                if (i>=regex.length || regex[i]!=')') { throw new Error("Missing ')'"); }
                i++; 
                break;
            default: i++;
        }
    }

    public void parse() {
        i = 1;
        expression();
        if (i != regex.length){ throw new Error("Illegal Expression"); }
        System.out.println(regex);
    }

    public static void main(String[] args) {
        if (args.length == 0 || !args[0].startsWith("\"") || !args[0].endsWith("\"")) { 
            System.out.println("Usage: java REcompile \\\" <expression> \\\"");
            args = new String[] {"\""+".a*((b)a)*\\?.|a*"+"\""};
            // return;
        }
        try { 
            REcompile RE = new REcompile(args[0]); 
        } catch (Error e) { System.err.println(e); }
    }
}
