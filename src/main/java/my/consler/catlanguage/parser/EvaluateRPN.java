package my.consler.catlanguage.parser;

import my.consler.catlanguage.token.Token;

import java.util.List;
import java.util.Stack;

public class EvaluateRPN
{
    public static int evaluate(List<Token> rpnTokens)
    {
        Stack<Integer> stack = new Stack<>();

        for (Token token : rpnTokens)
        {
            switch (token.getType())
            {
                case INTEGER:
                    stack.push(Integer.parseInt(token.getValue()));
                    break;
                case SYMBOL:
                    if (stack.size() < 2)
                    {
                        throw new RuntimeException("Invalid RPN expression: not enough operands for operator " + token.getValue());

                    }

                    int right = stack.pop();
                    int left = stack.pop();
                    int result = switch (token.getValue())
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
                        default -> throw new RuntimeException("Unknown operator: " + token.getValue());
                    };
                    stack.push(result);
                    break;
                default:
                    throw new RuntimeException("Unexpected token type in RPN: " + token.getType());

            }

        }

        if (stack.size() != 1)
        {
            throw new RuntimeException("Invalid RPN expression: stack should contain exactly one value");

        }

        return stack.pop();
    }

}
