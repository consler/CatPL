package consler.catlanguage.parser;

import consler.catlanguage.ast.AstNode;
import consler.catlanguage.ast.events.OnStart;
import consler.catlanguage.ast.statements.Assignment;
import consler.catlanguage.ast.statements.Statement;
import consler.catlanguage.ast.statements.calls.Log;
import consler.catlanguage.ast.statements.containers.If;
import consler.catlanguage.lexer.token.Token;
import consler.catlanguage.lexer.token.TokenType;

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

        while(currentIndex < tokens.size() - 1)
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
                throw new RuntimeException("Line: " + token.getLine() +". EVENT is expected, but got '" + token.getValue() + "' of type " + token.getType());

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

                    // System.out.println(tokens.get(currentIndex));

                    if(!(tokens.get(currentIndex).getType() == TokenType.INDETATION))
                    {
                        break;
                    }

                    currentIndex++;

                    if(tokens.get(currentIndex).getType() == TokenType.EVENT)
                    {
                        throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() + ". Nested events aren't allowed");
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
                case INDETATION ->
                {
                    currentIndex++;
                    return parseStatement();
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
            if(call.equals("log"))
            {
                if(tokens.get( currentIndex).getType() == TokenType.SYMBOL && tokens.get( currentIndex).getValue().equals( "(") ) return new Log( parseArguments());
                else throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". A parenthesis is expected after a function call, but got: " + tokens.get(currentIndex).getValue());
            }
            else if (call.equals("if"))
            {
                if(tokens.get( currentIndex).getType() == TokenType.SYMBOL && tokens.get( currentIndex).getValue().equals( "(") ) return parseIf();
                else throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". A parenthesis is expected after a function call, but got: " + tokens.get(currentIndex).getValue());
            }
            else
            {
                throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". Unknown function: " + call);
            }


        }
        else
        {
            throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". A parenthesis is expected after a function call");

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
                    if(tokens.get(currentIndex + 1).getType() == TokenType.KEYWORD || tokens.get(currentIndex+ 1).getType() == TokenType.EVENT || (tokens.get(currentIndex).getType() != TokenType.SYMBOL && tokens.get(currentIndex+1).getType() == TokenType.IDENTIFIER ) )
                    {
                        currentIndex--;
                        break;
                    }
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

    private static List<Token> parseArguments()
    {
        List<Token> expression_tokens = new ArrayList<>();
        expression_tokens.add(new Token(TokenType.SYMBOL, "(", tokens.get(currentIndex).getLine()));

        int parenthesis_count = 1;
        while (currentIndex+1 < tokens.size() && parenthesis_count > 0)
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
        return expression_tokens;
    }

    private static int indentationsBefore()
    {
        int indentations = 0;
        int indentations_check_index = currentIndex;

        while (indentations_check_index > 0 && tokens.get(indentations_check_index).getType() == TokenType.INDETATION)
        {
            indentations++;
            indentations_check_index--;
        }
        return indentations;
    }

    private static int indentationsInFront()
    {
        int indentations = 0;
        int indentations_check_index = currentIndex + 1;

        while (indentations_check_index < tokens.size() && tokens.get(indentations_check_index).getType() == TokenType.INDETATION)
        {
            indentations++;
            indentations_check_index++;
        }
        return indentations;
    }

    private static If parseIf()
    {
        List<Token> arguments = parseArguments();
        int indentations_before = indentationsBefore();
        List<Statement> then_block = new ArrayList<>();
        List<Statement> else_block = new ArrayList<>();

        currentIndex++;
        if (currentIndex < tokens.size())
        {
            if (tokens.get(currentIndex).getType() == TokenType.SYMBOL && tokens.get(currentIndex).getValue().equals(":"))
            {
                currentIndex++;
                while(currentIndex < tokens.size() && indentationsInFront() >= indentations_before+1)
                {
                    currentIndex += indentationsInFront() - indentations_before +1;
                    then_block.add(parseCall());
                    currentIndex++;
                }
                if(currentIndex < tokens.size() -1 & tokens.get(currentIndex + 1).getType() == TokenType.KEYWORD && tokens.get(currentIndex + 1).getValue().equals("else"))
                {
                    currentIndex += 2;
                    if(tokens.get(currentIndex).getType() == TokenType.SYMBOL && tokens.get(currentIndex).getValue().equals(":"))
                    {
                        while(currentIndex < tokens.size() && indentationsInFront() >= indentations_before+1)
                        {
                            currentIndex += indentationsInFront() - indentations_before +1;
                            else_block.add(parseCall());
                            currentIndex++;
                        }
                        currentIndex--;
                        return new If(arguments, then_block, else_block);
                    }
                    else
                    {
                        throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". A colon (:) is expected after an if statement");
                    }
                }
                else
                {
                    currentIndex--;
                    return new If(arguments, then_block);
                }
            }
            else
            {
                throw new RuntimeException("Line: " + tokens.get(currentIndex).getLine() +  ". A colon (:) is expected after an if statement");
            }
        }
        else
        {
            throw new RuntimeException("Unexpected token after: " + tokens.get(currentIndex-1));
        }
    }
}
