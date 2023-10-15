import java.util.ArrayList;
import java.util.Scanner;

public class Lab13_1
{
    // Converts an expression (as a String) in infix notation
    // to postfix notation (as an ArrayList of Strings)
    private static ArrayList<String> toPostfix(String infixStr) throws MismatchParenException
    {
        // Creates Scanner to scan over infixStr
        Scanner input = new Scanner(infixStr);

        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> stack = new ArrayList<>();

        while(input.hasNext())
        {
            String s = input.next();

            if(s.equals("("))
            {
                stack.add(stack.size() - 1, s);
            }
            else if(s.equals(")"))
            {
                while(!stack.get(stack.size() - 1).equals("("))
                {
                    if(stack.isEmpty()) throw new MismatchParenException();
                    output.add(output.size()-1, stack.get(stack.size()-1));
                    stack.remove(stack.size()-1);
                }
                stack.remove(stack.get(stack.size()-1));
            }
            else if(isOperator(s))
            {
                while(!stack.isEmpty())
                {
                    String top = stack.get(stack.size()-1);
                    if(!isOperator(top)) break;
                    if(precedence(top) < precedence(s)) break;

                    output.add(output.size() - 1, stack.get(stack.size()-1));
                    stack.remove(stack.size()-1);
                }
                stack.add(stack.size(), s);
            }
            else
            {
                output.add(output.size(), s);
            }
        }

        for(int i = stack.size() - 1; i >= 0; i--)
        {
            if(stack.get(i).equals(")")) throw new MismatchParenException();
            output.add(output.size() - 1, stack.get(i));
            stack.remove(i);

        }
        
        return output; // replace this line
    }

    // Evaluates the postfix expression.
    // The input is an ArrayList of tokens (Strings).
    private static double eval(ArrayList<String> postfix)
    {
        ArrayList<Double> stack = new ArrayList<>();

        for (String s : postfix)
        {
            switch (s)
            {
                case "+":
                    double b = stack.remove(stack.size() - 1);
                    double a = stack.get(stack.size() - 1);
                    stack.set(stack.size() - 1, a + b);
                    break;
                case "-":
                    b = stack.remove(stack.size() - 1);
                    a = stack.get(stack.size() - 1);
                    stack.set(stack.size() - 1, a - b);
                    break;
                case "*":
                    b = stack.remove(stack.size() - 1);
                    a = stack.get(stack.size() - 1);
                    stack.set(stack.size() - 1, a * b);
                    break;
                case "/":
                    b = stack.remove(stack.size() - 1);
                    a = stack.get(stack.size() - 1);
                    stack.set(stack.size() - 1, a / b);
                    break;
                default:
                    stack.add(Double.valueOf(s));
            }
        }

        if (stack.isEmpty()) return 0.0;

        // The result is on top of the stack
        return stack.get(stack.size() - 1);
    }

    // Tests if a token is an operator
    private static boolean isOperator(String s)
    {
        return s.equals("+") || s.equals("-") ||
                s.equals("*") || s.equals("/");
    }

    // Returns the precedence of an operator
    // or -1 if op is not an operator.
    private static int precedence(String op)
    {
        switch (op)
        {
            case "*":
            case "/": return 3;
            case "+":
            case "-": return 2;
            default:  return -1;
        }
    }

    // Concatenates the Strings in an ArrayList adding spaces in between
    private static String concatWithSpace(ArrayList<String> a)
    {
        StringBuilder output = new StringBuilder();

        for (int i=0; i < a.size(); i++)
        {
            if (i > 0) output.append(" ");

            output.append(a.get(i));
        }

        return output.toString();
    }

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        // Prompts user for expression in infix notation
        System.out.println("Enter infix expression (tokens separated by white space):");
        String infixStr = input.nextLine();

        // String to store result of converting to postfix
        ArrayList<String> postfix = null;

        try
        {
            postfix = toPostfix(infixStr); // convert to postfix
        }
        catch (MismatchParenException e)
        {
            // Error handling for mismatching parentheses
            System.err.println("Mismatched parentheses");
            System.exit(-1);
        }

        // Displays postfix
        System.out.println();
        System.out.println("Converted to postfix:");
        System.out.println(concatWithSpace(postfix));

        // Evaluates postfixStr and display the result
        System.out.println();

        try
        {
            System.out.println("The expression evaluates to: " + eval(postfix));
        }
        catch (NumberFormatException e)
        {
            // Error handling for operator argument not in numeric format
            System.err.println("Cannot evaluate expression: invalid argument");
            System.exit(-1);
        }
    }
}
