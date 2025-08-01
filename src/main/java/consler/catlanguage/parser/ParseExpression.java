package consler.catlanguage.parser;

import consler.catlanguage.execution.execute.assignment.Variable;
import consler.catlanguage.lexer.token.Token;
import consler.catlanguage.lexer.token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ParseExpression
{

    public static Object parse(List<Token> expression_tokens)
    {
        boolean isString = false;

        for (Token token : expression_tokens) //check whether contains a string
        {
            if (token.getType() == TokenType.STRING)
            {
                isString = true;
                break;

            }

        }

        for (int i = 0; i < expression_tokens.size(); i++) //checking
        {
            if(expression_tokens.get(i).getType() == TokenType.IDENTIFIER)
            {

                if (isString) expression_tokens.set(i, new Token(TokenType.STRING, Variable.getVariable( expression_tokens.get(i).getValue()),expression_tokens.get(i).getLine()));
                else expression_tokens.set(i, new Token(TokenType.INTEGER, Variable.getVariable( expression_tokens.get(i).getValue()), expression_tokens.get(i).getLine())); // replacing variables with their value
                // System.out.println("Replacing " + expression_tokens.get(i).getValue() + " with " + Variable.getVariable( expression_tokens.get(i).getValue()));

            }
            else if (expression_tokens.get(i).getType() == TokenType.SYMBOL )
            {
                if (isString && !( expression_tokens.get(i).getValue().equals("+") || expression_tokens.get(i).getValue().equals("(") || expression_tokens.get(i).getValue().equals(")") ))
                {
                    throw new RuntimeException("Line: " + expression_tokens.get(i).getLine() +  ". A an unexpected symbol received, when trying to concatenate a string.");

                }
                else if(!isString && !(
                        expression_tokens.get(i).getValue().equals("+") ||
                                expression_tokens.get(i).getValue().equals("-")  ||
                                expression_tokens.get(i).getValue().equals("*")  ||
                                expression_tokens.get(i).getValue().equals("/") ||
                                expression_tokens.get(i).getValue().equals("(") ||
                                expression_tokens.get(i).getValue().equals(")")))
                    throw new RuntimeException("Line: " + expression_tokens.get(i).getLine() +  ". An unexpected symbol received, when trying to calculate an expression: " + expression_tokens.get(i).getValue());


            }
            else if (expression_tokens.get(i).getType() == TokenType.KEYWORD || expression_tokens.get(i).getType() == TokenType.EVENT)
            {
                throw new RuntimeException("Line: " + expression_tokens.get(i).getLine() +  ". An unexpected token type, when trying to calculate an expression");

            }

        }

        // System.out.println(expression_tokens);

        if(isString)
        {
            return parseString(expression_tokens);
        }
        else
        {
            float result = parseArithmeticExpression(expression_tokens);

            if(result == (int) result)
            {
                return (int) result;
            }
            else
            {
                return result;
            }
        }

    }
    public static float parseArithmeticExpression(List<Token> expression_tokens)
    {
        Stack<Token> operator_stack = new Stack<>();
        List<Token> output = new ArrayList<>(); // Use List instead of Stack for output

        for (Token token : expression_tokens)
        {
            switch (token.getType())
            {
                case INTEGER -> output.add(token);
                case SYMBOL ->
                {
                    if (token.getValue().equals("("))
                    {
                        operator_stack.push(token);

                    }
                    else if (token.getValue().equals(")"))
                    {
                        while (!operator_stack.isEmpty() && !operator_stack.peek().getValue().equals("("))
                        {
                            output.add(operator_stack.pop());

                        }
                        operator_stack.pop(); // Pop the left parenthesis

                    }
                    else
                    {
                        operator_stack = addOperator(operator_stack, output, token);

                    }

                }

            }

        }

        // Pop all the operators from the stack
        while (!operator_stack.isEmpty())
        {
            Token top = operator_stack.pop();
            if (!top.getValue().equals("("))
            { // Ensure we don't add the left parenthesis
                output.add(top);

            }

        }

        return evaluateRPN(output); // Return the output in postfix notation

    }

    public static boolean parseCondition(List<Token> condition_tokens)
    {
        // System.out.println(condition_tokens);

        condition_tokens = condition_tokens.subList(1, condition_tokens.size() -1); // getting rid of the first and last parenthesis

        String MODE = "NOT_VALID";
        int operand_index = 0;

        for (int i = 0; i < condition_tokens.size()-1; i++)
        {
            Token token = condition_tokens.get(i);
            Token next_token = condition_tokens.get(i + 1);
            //System.out.println(token + " " + next_token);

            if(token.getType() == TokenType.SYMBOL && token.getValue().equals("=") && next_token.getType() == TokenType.SYMBOL && next_token.getValue().equals("=") ) // ==
            {
                MODE = "EQUAL";
                operand_index = i;
                break;

            }
            else if(token.getType() == TokenType.SYMBOL && token.getValue().equals("!") && next_token.getType() == TokenType.SYMBOL && next_token.getValue().equals("=") ) // !=
            {
                MODE = "NEGATED_EQUAL";
                operand_index = i;
                break;

            }
            else if(token.getType() == TokenType.SYMBOL && token.getValue().equals(">") ) // >
            {
                MODE = "GREATER_THAN";
                operand_index = i;
                break;

            }
            else if(token.getType() == TokenType.SYMBOL && token.getValue().equals("<") ) // <
            {
                MODE = "LESS_THAN";
                operand_index = i;
                break;

            }
            else if (token.getType() == TokenType.SYMBOL)
            {
                throw new RuntimeException("Line: " + token.getLine() +  ". Unexpected symbol: " + token.getValue());

            }

        }

        if(MODE.equals("NOT_VALID")) throw new RuntimeException("Line: " + condition_tokens.getFirst() + ". Invalid condition");

        if(operand_index == 0) throw new RuntimeException("Line: " + condition_tokens.getFirst().getLine() + ". Condition can't start with an operand");


        List<Token> left_side = condition_tokens.subList(0, operand_index);

        switch(MODE)
        {
            case "EQUAL" ->
            {
                List<Token> right_side = condition_tokens.subList(operand_index+2, condition_tokens.size());
                if(ParseExpression.parse(left_side).equals(ParseExpression.parse(right_side))) return true;

            }
            case "NEGATED_EQUAL" ->
            {
                List<Token> right_side = condition_tokens.subList(operand_index+2, condition_tokens.size());
                if(!ParseExpression.parse(left_side).equals(ParseExpression.parse(right_side))) return true;

            }
            case "GREATER_THAN" ->
            {
                List<Token> right_side = condition_tokens.subList(operand_index+1, condition_tokens.size());
                try
                {
                    if((int) ParseExpression.parse(left_side) > (int) ParseExpression.parse(right_side)) return true;
                }
                catch (NumberFormatException n)
                {
                    throw new RuntimeException("Line: " + condition_tokens.getFirst().getLine() +  ". Invalid comparison: " + condition_tokens.getFirst().getValue());
                }

            }
            case "LESS_THAN" ->
            {
                List<Token> right_side = condition_tokens.subList(operand_index+1, condition_tokens.size());
                try
                {
                    if((int) ParseExpression.parse(left_side) < (int) ParseExpression.parse(right_side)) return true;
                }
                catch (NumberFormatException n)
                {
                    throw new RuntimeException("Line: " + condition_tokens.getFirst().getLine() +  ". Invalid comparison: " + condition_tokens.getFirst().getValue());
                }
            }

        }

        return false;

    }

    public static String parseString(List<Token> string_tokens)
    {
        StringBuilder string = new StringBuilder();

        for (int i = 0; i < string_tokens.size(); i++)
        {
            Token token = string_tokens.get(i);

            if (token.getType() == TokenType.EVENT || token.getType() == TokenType.IDENTIFIER )
            {
                throw new RuntimeException("Line: " + token.getLine() +  ". Unexpected error: Event or identifier, when trying to concatenate a string");

            }
            else if (token.getType() == TokenType.SYMBOL)
            {
                if (token.getValue().equals("(") || token.getValue().equals(")"))
                {
                    continue;

                }

                if(!token.getValue().equals("+"))
                {
                    throw new RuntimeException("Line: " + token.getLine() +  ". Unexpected symbol, when trying to concatenate a string");

                }

                if (string_tokens.size() < i)
                {
                    throw new RuntimeException("Line: " + token.getLine() +  ". A string concatenation can't end with a +");

                }

            }
            else if (token.getType() == TokenType.STRING || token.getType() == TokenType.INTEGER)
            {
                string.append(token.getValue());

            }

        }

        return string.toString();


    }

    private static Stack<Token> addOperator(Stack<Token> operator_stack, List<Token> output, Token token)
    {
        while (!operator_stack.isEmpty() && precedence(operator_stack.peek()) >= precedence(token))
        {
            output.add(operator_stack.pop());

        }
        operator_stack.push(token);
        return operator_stack;

    }

    private static int precedence(Token token)
    {
        return switch (token.getValue())
        {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> 0; // Non-operators have the lowest precedence
        };

    }

    public static float evaluateRPN(List<Token> rpnTokens)
    {
        Stack<Float> stack = new Stack<>();

        for (Token token : rpnTokens)
        {
            switch (token.getType())
            {
                case INTEGER:
                    stack.push(Float.parseFloat(token.getValue()));
                    break;
                case SYMBOL:
                    if (stack.size() < 2)
                    {
                        throw new RuntimeException("Invalid RPN expression: not enough operands for operator " + token.getValue());

                    }

                    float right = stack.pop();
                    float left = stack.pop();
                    float result = switch (token.getValue())
                    {
                        case "+" -> left + right;
                        case "-" -> left - right;
                        case "*" -> left * right;
                        case "/" -> {
                            if (right == 0)
                            {
                                throw new RuntimeException("Division by zero");
                            }
                            yield left / right;
                        }
                        default -> throw new RuntimeException("Line: " + token.getLine() + ". Unknown operator: " + token.getValue());
                    };
                    stack.push(result);
                    break;
                default:
                    throw new RuntimeException("Line: " + token.getLine() + ". Unexpected token type in RPN: " + token.getType());

            }

        }

        if (stack.size() != 1)
        {
            throw new RuntimeException("Line: " + rpnTokens.getFirst().getLine() + "Invalid RPN expression: stack should contain exactly one value");

        }

        return stack.pop();
    }

}
