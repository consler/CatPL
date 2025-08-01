package consler.catlanguage.parser;

import consler.catlanguage.ast.AstNode;
import consler.catlanguage.ast.events.OnStart;
import consler.catlanguage.ast.statements.Assignment;
import consler.catlanguage.ast.statements.Statement;
import consler.catlanguage.ast.statements.calls.Log;
import consler.catlanguage.token.Token;
import consler.catlanguage.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser
{
    private static int currentIndex = 0;
    private static List<Token> tokens;
    public static List<AstNode> parse(List<Token> tokens)
    {
        Parser.tokens = tokens;
        List<AstNode> astNodes = new ArrayList<>();

        while(currentIndex < tokens.size())
        {
            Token token = tokens.get(currentIndex);

            if (token.getType() == TokenType.EVENT)
            {
                if (token.getValue().equals("onStart"))
                {
                    currentIndex++;
                    astNodes.add(parseOnStart());

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

        return astNodes;


    }

    private static AstNode parseOnStart()
    {
        List<Statement> statements = new ArrayList<>();

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
                        statements.add(parseStatement());

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

        return new OnStart(statements);

    }

    private static Statement parseStatement()
    {
        //System.out.println("Parsing statement");

        if (currentIndex < tokens.size())
        {
            Token token = tokens.get(currentIndex);
            //System.out.println(token);
            switch (token.getType())
            {
                case KEYWORD ->
                {
                    return parseCall();
                }
                case IDENTIFIER ->
                {
                    return parseAssignment();
                }
                default -> throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() + ". Unexpected token: " + token.getValue());
            }

        }
        else
        {
            throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". Unexpected end of input.");

        }

    }

    private static Statement parseCall()
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
                    return new Log( expression_tokens);


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

    private static Assignment parseAssignment()
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

                return new Assignment(identifier, expression_tokens);

            }
            else
            {
                throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". A symbol is expected after an identifier, but got: " + tokens.get(currentIndex).getValue());

            }

        }
        else
        {
            throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". Unexpected end of input.");

        }

    }



}
