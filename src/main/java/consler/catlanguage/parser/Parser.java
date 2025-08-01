package consler.catlanguage.parser;

import consler.catlanguage.execution.functions.Log;
import consler.catlanguage.execution.Variable;
import consler.catlanguage.token.Token;
import consler.catlanguage.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser
{
    private static int currentIndex = 0;
    private static List<Token> tokens;
    public static void parse(List<Token> tokens)
    {
        Parser.tokens = tokens;
        while(currentIndex < tokens.size())
        {
            Token token = tokens.get(currentIndex);

            if (token.getType() == TokenType.EVENT)
            {
                if (token.getValue().equals("onStart"))
                {
                    currentIndex++;
                    parseOnStart();

                }
                else if(token.getValue().equals("onClick"))
                {
                    currentIndex++;
                    throw new RuntimeException("Line: " + token.getLine() + ". Cant parse onClick event yet");

                }
                else
                {
                    throw new RuntimeException("Line: " + token.getLine() + ". Unknown event: " + token.getValue());

                }

            }
            else
            {
                currentIndex++;
            }


        }


    }

    private static void parseOnStart()
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
                        parseStatement();

                    }
                    else break;

                }

            }
            else
            {
                throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". A colon (:) is expected after an event declaration, but got: " + tokens.get(currentIndex).getValue());

            }

        }
        else
        {
            throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". A colon (:) is expected after an event declaration.");

        }

    }

    private static void parseStatement()
    {
        //System.out.println("Parsing statement");

        if (currentIndex < tokens.size())
        {
            Token token = tokens.get(currentIndex);
            //System.out.println(token);
            switch (token.getType())
            {
                case KEYWORD -> parseCall();
                case IDENTIFIER -> parseAssignment();
                default -> throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() + ". Unexpected token: " + token.getValue());
            }

        }
        else
        {
            throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". Unexpected end of input.");

        }


    }

    private static void parseCall()
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
                    expression_tokens.add(new Token(TokenType.SYMBOL, "(", tokens.get(currentIndex).getLine()));

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
                    throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". Unknown function: " + call);

                }

            }
            else
            {
                throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". A parenthesis is expected after a function call.");
            }
        }
        else
        {
            throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". A parenthesis is expected after a function call.");

        }

    }

    private static void parseAssignment()
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
            throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". Unexpected end of input.");

        }

    }

    private static Object parseExpression(List<Token> expression_tokens)
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

                if (isString) expression_tokens.set(i, new Token(TokenType.STRING, Variable.getVariable( expression_tokens.get(i).getValue()), tokens.get(currentIndex).getLine()));
                else expression_tokens.set(i, new Token(TokenType.INTEGER, Variable.getVariable( expression_tokens.get(i).getValue()), tokens.get(currentIndex).getLine())); // replacing variables with their value
                // System.out.println("Replacing " + expression_tokens.get(i).getValue() + " with " + Variable.getVariable( expression_tokens.get(i).getValue()));

            }
            else if (expression_tokens.get(i).getType() == TokenType.SYMBOL )
            {
                if (isString && !( expression_tokens.get(i).getValue().equals("+") || expression_tokens.get(i).getValue().equals("(") || expression_tokens.get(i).getValue().equals(")") ))
                {
                    throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". A an unexpected symbol received, when trying to concatenate a string.");

                }
                else if(!isString && !(
                        expression_tokens.get(i).getValue().equals("+") ||
                        expression_tokens.get(i).getValue().equals("-")  ||
                        expression_tokens.get(i).getValue().equals("*")  ||
                        expression_tokens.get(i).getValue().equals("/") ||
                        expression_tokens.get(i).getValue().equals("(") ||
                        expression_tokens.get(i).getValue().equals(")")))
                    throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". An unexpected symbol received, when trying to calculate an expression: " + expression_tokens.get(i).getValue());


            }
            else if (expression_tokens.get(i).getType() == TokenType.KEYWORD || expression_tokens.get(i).getType() == TokenType.EVENT)
            {
                throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". An unexpected token type, when trying to calculate an expression");

            }

        }

        // System.out.println(expression_tokens);

        if(isString) return ParseExpression.parseString(expression_tokens);
        else return ParseExpression.parseArithmeticExpression(expression_tokens);

    }

}
