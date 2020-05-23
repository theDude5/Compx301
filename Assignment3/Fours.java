import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Fours {
    Queue<String> frontier;

    public Fours(Double input) {
        frontier = new LinkedList<String>();
        String firstState = "4";
        String finalString = "";
        frontier.add(firstState);
        double d = 0;
        while(d != input) {
            try {
                String s = frontier.remove();
                finalString = s;
                d = EvaluateString.evaluate(s);
                expand(s);
            }
            catch (Exception e) {
            }
            catch (StackOverflowError t) {
                System.out.println("Could not evaluate to " + input);
            }
        }
        System.out.println(finalString + " is equivalent to the input of " + input);
    }

    public void expand(String s) {
        frontier.add(s + " + 4");
        frontier.add(s + " - 4");
        frontier.add(s + " * 4");
        frontier.add(s + " / 4");
        frontier.add(s + "4");
        frontier.add(s + ".4");
        frontier.add("(" + s + ")");
    }

    public static void main(String [] args) {
        String input = args[0];
        Double dInput = Double.parseDouble(input);
        Fours bfs = new Fours(dInput);
    }
}

class EvaluateString
{
    public static double evaluate(String expression)
    {
        char[] tokens = expression.toCharArray();

        // Stack for numbers: 'values'
        Stack<Double> values = new Stack<Double>();

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
                values.push(Double.parseDouble(sbuf.toString()));
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
    public static double applyOp(char op, double b, double a)
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
            case '.':
                if(a % 1 == 0 && b % 1 == 0) {
                    if(b < 10) {
                        b = b / 10;
                    }
                    else if (b == 100){
                        b = 0.1;
                    }
                    else {
                        b = b / 100;
                    }
                    return a + b;
                }
        }
        return 0;
    }
}