import java.util.*;

public class BFS {
    Queue<String> frontier;

    public BFS(String input) {
        frontier = new LinkedList<String>();
        String firstState = "4";
        frontier.add(firstState);
        boolean go = true;
        while(go) {
            try {
                String s = frontier.remove();
                if(Integer.parseInt(input) == EvaluateString.evaluate(s)) {
                    System.out.println(s + " is equivalent to the input of " + input);
                    go = false;
                }
                else {
                    String child0 = s + "+4";
                    frontier.add(child0);
                    String child1 = s + "-4";
                    frontier.add(child1);
                    String child2 = s + "*4";
                    frontier.add(child2);
                    String child3 = s + "/4";
                    frontier.add(child3);
                    String child4 = s + "4";
                    frontier.add(child4);
                    String child5 = s + ".4";
                    frontier.add(child5);
                    String child6 = s + "(" + s + ")";
                    frontier.add(child6);
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }


    public static void main(String [] args) {
        String input = args[0];
        BFS bfs = new BFS(input);
    }
}

class EvaluateString
{
    public static int evaluate(String expression)
    {
        char[] tokens = expression.toCharArray();

        // Stack for numbers: 'values'
        Stack<Integer> values = new Stack<Integer>();

        // Stack for Operators: 'ops'
        Stack<Character> ops = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++)
        {
            // Current token is a number, push it to stack for numbers
            if (tokens[i] >= '0' && tokens[i] <= '9')
            {
                StringBuffer sbuf = new StringBuffer();
                // There may be more than one digits in number
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9')
                    sbuf.append(tokens[i++]);
                values.push(Integer.parseInt(sbuf.toString()));
            }

            // Current token is an opening brace, push it to 'ops'
            else if (tokens[i] == '(')
                ops.push(tokens[i]);

                // Closing brace encountered, solve entire brace
            else if (tokens[i] == ')')
            {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.pop();
            }

            // Current token is an operator.
            else if (tokens[i] == '+' || tokens[i] == '-' ||
                    tokens[i] == '*' || tokens[i] == '/' ||
                    tokens[i] == '.')
            {
                // While top of 'ops' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));

                // Push current token to 'ops'.
                ops.push(tokens[i]);
            }
        }

        // Entire expression has been parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));

        // Top of 'values' contains result, return it
        return values.pop();
    }

    // Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
    public static boolean hasPrecedence(char op1, char op2)
    {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    // A utility method to apply an operator 'op' on operands 'a'
    // and 'b'. Return the result.
    public static int applyOp(char op, int b, int a)
    {
        switch (op)
        {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                return a / b;
            //case '.':
               // return a.b;
        }
        return 0;
    }
}
