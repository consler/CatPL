package my.consler.catlanguage.parser;

import my.consler.catlanguage.execution.Log;
import my.consler.catlanguage.execution.Variable;
import my.consler.catlanguage.token.Token;
import my.consler.catlanguage.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser
{
    private static int currentIndex = 0;
    public static void parse(List<Token> tokens)
    {
        while(currentIndex < tokens.size())
        {
            Token token = tokens.get(currentIndex);

            if (token.getType() == TokenType.EVENT)
            {
                currentIndex++;
                parseEvent(tokens);

            }
            else
            {
                currentIndex++;
            }


        }


    }

    private static void parseEvent(List<Token> tokens)
    {
        if(currentIndex <  tokens.size())
        {

            //System.out.println("Parsing event: " + tokens.get(currentIndex - 1).getValue());

            if(tokens.get(currentIndex).getType() == TokenType.SYMBOL && tokens.get(currentIndex).getValue().equals(":"))
            {
                while(currentIndex + 1 < tokens.size())
                {
                    currentIndex++;
                    if(tokens.get(currentIndex).getType() == TokenType.EVENT)
                    {
                        break;
                    }

                    if (currentIndex + 1  < tokens.size())
                    {
                        parseStatement(tokens);

                    }
                    else break;

                }

            }
            else
            {
                throw new RuntimeException("A colon (:) is expected after an event declaration, but got: " + tokens.get(currentIndex).getValue());

            }

        }
        else
        {
            throw new RuntimeException("A colon (:) is expected after an event declaration.");

        }

    }

    private static void parseStatement(List<Token> tokens)
    {
        //System.out.println("Parsing statement");

        if (currentIndex < tokens.size())
        {
            Token token = tokens.get(currentIndex);
            //System.out.println(token);
            switch (token.getType())
            {
                case KEYWORD -> parseCall(tokens);
                case IDENTIFIER -> parseAssignment(tokens);
                default -> throw new RuntimeException("Unexpected token: " + token.getValue());
            }

        }
        else
        {
            throw new RuntimeException("Unexpected end of input.");

        }


    }

    private static void parseCall(List<Token> tokens)
    {
        String call =  tokens.get(currentIndex).getValue();
        //System.out.println("Parsing function call: " + tokens.get(currentIndex).getValue());
        currentIndex++;
        if (currentIndex < tokens.size())
        {
            if(tokens.get( currentIndex).getType() == TokenType.SYMBOL && tokens.get( currentIndex).getValue().equals( "(") )
            {

                if(call.equals("log"))
                {
                    List<Token> expression_tokens = new ArrayList<>();
                    expression_tokens.add(new Token(TokenType.SYMBOL, "("));

                    int parenthesis_count = 1;
                    while (currentIndex < tokens.size()  && parenthesis_count > 0)
                    {

                        currentIndex++;
                        if(tokens.get(currentIndex).getValue().equals("("))
                        {
                            parenthesis_count++;

                        }
                        else if(tokens.get(currentIndex).getValue().equals(")"))
                        {
                            parenthesis_count--;

                        }

                        expression_tokens.add(tokens.get(currentIndex));

                    }
                    //currentIndex++;
                    Log.log( String.valueOf( parseExpression(expression_tokens)));

                }
                else
                {
                    throw new RuntimeException("Unknown function: " + call);

                }

            }
            else
            {
                throw new RuntimeException("A parenthesis is expected after a function call.");
            }
        }
        else
        {
            throw new RuntimeException("A parenthesis is expected after a function call.");

        }

    }

    private static void parseAssignment(List<Token> tokens)
    {
        String identifier = tokens.get(currentIndex).getValue();

        //System.out.println("Parsing assignment: " + identifier);
        currentIndex++;
        if (currentIndex < tokens.size())
        {
            if (tokens.get(currentIndex).getType() == TokenType.SYMBOL && tokens.get(currentIndex).getValue().equals("="))
            {
                List<Token> expression_tokens = new ArrayList<>();
                while (currentIndex < tokens.size())
                {
                    currentIndex++;
                    expression_tokens.add(tokens.get(currentIndex));
                    //System.out.println("current index: " + currentIndex + " " + tokens.get(currentIndex));
                    if(
                        tokens.get(currentIndex + 1).getType() == TokenType.IDENTIFIER ||
                        tokens.get(currentIndex + 1).getType() == TokenType.KEYWORD ||
                        ( tokens.get(currentIndex+ 1).getType() == TokenType.EVENT && tokens.get(currentIndex).getValue().equals(")") )
                    ) break;


                }

                //System.out.println("expression: " + expression_tokens);
                Variable.setVariable(identifier, String.valueOf( parseExpression( expression_tokens)));

            }

        }
        else
        {
            throw new RuntimeException("Unexpected end of input.");

        }

    }

    private static Object parseExpression(List<Token> expression_tokens)
    {
        boolean isString = false;
        for (Token token : expression_tokens)
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

                if (isString)
                {
                    expression_tokens.set(i, new Token(TokenType.STRING, Variable.getVariable( expression_tokens.get(i).getValue())));

                }
                else
                {
                    expression_tokens.set(i, new Token(TokenType.INTEGER, Variable.getVariable( expression_tokens.get(i).getValue())));

                }
                // System.out.println("Replacing " + expression_tokens.get(i).getValue() + " with " + Variable.getVariable( expression_tokens.get(i).getValue()));

            }
            else if (expression_tokens.get(i).getType() == TokenType.SYMBOL )
            {
                if (isString && !( expression_tokens.get(i).getValue().equals("+") || expression_tokens.get(i).getValue().equals("(") || expression_tokens.get(i).getValue().equals(")") ))
                {
                    throw new RuntimeException("A an unexpected symbol received, when trying to concatenate a string.");

                }
                else if (!isString)
                {
                    if(!(   expression_tokens.get(i).getValue().equals("+") ||
                            expression_tokens.get(i).getValue().equals("-")  ||
                            expression_tokens.get(i).getValue().equals("*")  ||
                            expression_tokens.get(i).getValue().equals("/") ||
                            expression_tokens.get(i).getValue().equals("(") ||
                            expression_tokens.get(i).getValue().equals(")")
                    ))
                    {
                        throw new RuntimeException("An unexpected symbol received, when trying to calculate an expression: " + expression_tokens.get(i).getValue());

                    }
                }

            }
            else if (expression_tokens.get(i).getType() == TokenType.KEYWORD || expression_tokens.get(i).getType() == TokenType.EVENT)
            {
                throw new RuntimeException("An unexpected token type, when trying to calculate an expression");

            }

        }

        // System.out.println(expression_tokens);

        if(!isString)
        {
            List<Token> rpnTokens = ParseExpression.parse(expression_tokens);
            return EvaluateRPN.evaluate(rpnTokens);

        }
        else
        {
            StringBuilder string = new StringBuilder();

            for (int i = 0; i < expression_tokens.size(); i++)
            {
                Token token = expression_tokens.get(i);

                if (token.getType() == TokenType.EVENT || token.getType() == TokenType.IDENTIFIER )
                {
                    throw new RuntimeException("Unexpected error: Event or identifier, when trying to concatenate a string");

                }
                else if (token.getType() == TokenType.SYMBOL)
                {
                    if (token.getValue().equals("(") || token.getValue().equals(")"))
                    {
                        continue;

                    }

                    if(!token.getValue().equals("+"))
                    {
                        throw new RuntimeException("Unexpected symbol, when trying to concatenate a string");

                    }

                    if (expression_tokens.size() < i)
                    {
                        throw new RuntimeException("A string concatenation can't end with a +");

                    }

                }
                else if (token.getType() == TokenType.STRING || token.getType() == TokenType.INTEGER)
                {
                    string.append(token.getValue());

                }

            }

            return string.toString();

        }


    }

    private boolean parseCondition(List<Token> condition_tokens)
    {

        return false;
    }

}
