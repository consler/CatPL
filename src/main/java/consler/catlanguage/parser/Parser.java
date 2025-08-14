package consler.catlanguage.parser;

import consler.catlanguage.ast.AstNode;
import consler.catlanguage.ast.events.OnStart;
import consler.catlanguage.ast.statements.Assignment;
import consler.catlanguage.ast.statements.Statement;
import consler.catlanguage.ast.statements.calls.Log;
import consler.catlanguage.ast.statements.calls.Print;
import consler.catlanguage.ast.statements.containers.If;
import consler.catlanguage.ast.statements.containers.While;
import consler.catlanguage.lexer.token.Token;
import consler.catlanguage.lexer.token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static consler.catlanguage.Main.debug;

public class Parser
{
    public static int currentIndex = 0;
    public static List<Token> tokens;
    public static List<AstNode> parse(List<Token> tokens)
    {
        Parser.tokens = tokens;
        List<AstNode> astNodes = new ArrayList<>();

        while(currentIndex < tokens.size() - 1)
        {
            Token token = tokens.get(currentIndex);

            if (token.getType() != TokenType.EVENT) new ParsingError("EVENT is expected, but got " + token.getValue() + " of " + token.getType());

            if (token.getValue().equals("onStart")) astNodes.add(parseOnStart());
            else if(token.getValue().equals("onClick")) new ParsingError("OnClick not implemented yet");
            else new ParsingError("Unknown event: '" + token.getValue());

        }
        return astNodes;
    }

    private static AstNode parseOnStart()
    {
        if(debug) System.out.println("Parsing onStart");
        currentIndex++; // onStart -> :
        List<Statement> statements = new ArrayList<>();

        if(isTokenNotColon()) new ParsingError("A colon (:) is expected after an event declaration, but got: " + tokens.get(currentIndex).getValue());
        currentIndex++; // : -> EOL

        while(currentIndex + 1< tokens.size())
        {
            currentIndex++;

            if(!(tokens.get(currentIndex).getType() == TokenType.INDETATION)) break; // break if event is over

            currentIndex++; // indentation -> statement

            if(tokens.get(currentIndex).getType() == TokenType.EVENT) new ParsingError("Nested events aren't allowed");


            if (currentIndex + 1  < tokens.size())
            {
                statements.add(parseStatement());
            }
            else break;
        }
        return new OnStart(statements);
    }

    private static Statement parseStatement()
    {
        if(debug) System.out.println("Parsing statement");

        if (!(currentIndex < tokens.size())) new ParsingError("Unexpected end of input");

        Token token = tokens.get(currentIndex);
        switch (token.getType())
        {
            case KEYWORD: return parseCall();
            case IDENTIFIER: return parseAssignment();
            case INDETATION: new ParsingError("Unexpected error: got indentation as a statement. Please report this error along with your code");
            case EOL: new ParsingError("Unexpected end of line");
            default: new ParsingError("Unexpected token: " + token.getValue());
        }
        return null;
    }

    private static Statement parseCall()
    {
        String call =  tokens.get(currentIndex).getValue();
        if(debug) System.out.println("Parsing function call: " + tokens.get(currentIndex).getValue());
        currentIndex++;
        if (!(currentIndex < tokens.size())) new ParsingError("A parenthesis is expected after a function call");

        switch (call)
        {
            case "log" ->
            {
                if (tokens.get(currentIndex).getType() == TokenType.SYMBOL && tokens.get(currentIndex).getValue().equals("("))
                    return new Log(parseArguments(true));
                else
                    new ParsingError("A parenthesis is expected after a function call, but got: " + tokens.get(currentIndex).getValue());
            }
            case "print" ->
            {
                if (tokens.get(currentIndex).getType() == TokenType.SYMBOL && tokens.get(currentIndex).getValue().equals("("))
                    return new Print(parseArguments(true));
                else
                    new ParsingError("A parenthesis is expected after a function call, but got: " + tokens.get(currentIndex).getValue());
            }
            case "if" ->
            {
                if (tokens.get(currentIndex).getType() == TokenType.SYMBOL && tokens.get(currentIndex).getValue().equals("("))
                    return parseIf();
                else
                    new ParsingError("A parenthesis is expected after a function call, but got: " + tokens.get(currentIndex).getValue());
            }
            case "while" ->
            {
                if (tokens.get(currentIndex).getType() == TokenType.SYMBOL && tokens.get(currentIndex).getValue().equals("("))
                    return parseWhile();
                else
                    new ParsingError("A parenthesis is expected after a function call, but got: " + tokens.get(currentIndex).getValue());
            }
            default -> new ParsingError("Unexpected error because of an unknown function call: " + tokens.get(currentIndex).getValue() + ". Please report the error along with your code");
        }
        return null;
    }

    private static Assignment parseAssignment()
    {
        String identifier = tokens.get(currentIndex).getValue();

        if(debug) System.out.println("Parsing assignment: " + identifier);
        currentIndex++;
        if (isTokenEOLorEOF())
            new ParsingError("Unexpected end of input.");

        if (tokens.get(currentIndex).getType() == TokenType.SYMBOL && (tokens.get(currentIndex).getValue().equals("="))) // for variables
        {
            List<Token> expression_tokens = new ArrayList<>();
            while (currentIndex < tokens.size())
            {
                currentIndex++;
                expression_tokens.add(tokens.get(currentIndex));
                if(tokens.get(currentIndex+1).getType() == TokenType.EOL)
                {
                    currentIndex++;
                    break;
                }
                if(tokens.get(currentIndex + 1).getType() == TokenType.KEYWORD || tokens.get(currentIndex+ 1).getType() == TokenType.EVENT )
                    new ParsingError("Unexpected token: " + tokens.get(currentIndex).getValue());
            }
            return new Assignment(identifier, expression_tokens);
        }
        else if(tokens.get(currentIndex).getType() == TokenType.SYMBOL && (tokens.get(currentIndex).getValue().equals("["))) // for tables
        {
            currentIndex++;
            if(tokens.get(currentIndex).getType() == TokenType.SYMBOL && (tokens.get(currentIndex).getValue().equals("]")))
            {
                currentIndex++;
                return new Assignment(identifier, null, null);
            }
            else if(tokens.get(currentIndex).getType() == TokenType.IDENTIFIER || tokens.get(currentIndex).getType() == TokenType.STRING || tokens.get(currentIndex).getType() == TokenType.NUMBER)
            {
                List<Token> index = parseTableArguments();
                if(!(tokens.get(currentIndex).getType() == TokenType.SYMBOL && (tokens.get(currentIndex).getValue().equals("]"))))
                    new ParsingError("A closing bracket is expected after a table index, but got: " + tokens.get(currentIndex).getValue());
                currentIndex++; // ] -> =
                if (!(tokens.get(currentIndex).getType() == TokenType.SYMBOL && (tokens.get(currentIndex).getValue().equals("="))))
                    new ParsingError("An assignment token expected, but got: " + tokens.get(currentIndex).getValue());
                List<Token> expression_tokens = new ArrayList<>();
                while (currentIndex < tokens.size())
                {
                    currentIndex++;
                    expression_tokens.add(tokens.get(currentIndex));
                    if(tokens.get(currentIndex+1).getType() == TokenType.EOL)
                    {
                        currentIndex++;
                        break;
                    }
                    if(tokens.get(currentIndex + 1).getType() == TokenType.KEYWORD || tokens.get(currentIndex+ 1).getType() == TokenType.EVENT )
                        new ParsingError("Unexpected token: " + tokens.get(currentIndex).getValue());
                }
                return new Assignment(identifier, index, expression_tokens);

            }
            else
                new ParsingError("A closing bracket is expected after a table index, but got: " + tokens.get(currentIndex).getValue());
        }
        else
            new ParsingError("An assignment token expected, but got: " + tokens.get(currentIndex).getValue());

        return null;
    }

    private static List<Token> parseArguments(boolean EOL)
    {
        List<Token> expression_tokens = new ArrayList<>();
        expression_tokens.add(new Token(TokenType.SYMBOL, "(", tokens.get(currentIndex).getLine()));

        int parenthesis_count = 1;
        while (currentIndex+1 < tokens.size() && parenthesis_count > 0)
        {
            currentIndex++;
            if(tokens.get(currentIndex).getValue().equals("("))
                parenthesis_count++;
            else if(tokens.get(currentIndex).getValue().equals(")"))
                parenthesis_count--;
            expression_tokens.add(tokens.get(currentIndex));
        }
        if(EOL) currentIndex++;

        return expression_tokens;
    }

    private static int getIndentations(int line)
    {
        int indentation_count = 0;

        for(Token token : tokens)
        {
            if(token.getLine() == line)
            {
                if(token.getType() == TokenType.INDETATION)
                    indentation_count++;
                else
                    break;
            }
            else if(token.getLine() > line)
                break;
        }
        return indentation_count;
    }


    private static If parseIf()
    {
        int indentations_before_if = getIndentations(tokens.get(currentIndex).getLine());
        List<Token> arguments = parseArguments(false);
        List<Statement> then_block = new ArrayList<>();
        List<Statement> else_block = new ArrayList<>();

        currentIndex++; // ) -> :

        if (isTokenNotColon()) new ParsingError("A colon (:) is expected after an if statement, but got " + tokens.get(currentIndex).getValue() + " of " + tokens.get(currentIndex).getType());

        currentIndex+=2; // : -> eol -> indentation
        if(debug) System.out.println("Line " + tokens.get(currentIndex).getLine() + ": Indentations in front: " + getIndentations(tokens.get(currentIndex).getLine() +1 ) + ", indentations before: " + getIndentations(tokens.get(currentIndex).getLine()));

        while(currentIndex < tokens.size() && getIndentations(tokens.get(currentIndex).getLine()) > indentations_before_if)
        {
            currentIndex += getIndentations(tokens.get(currentIndex).getLine()); // skip indentations
            then_block.add(parseStatement());
            currentIndex++;
        }
        currentIndex--;

        // else stuff
        if(currentIndex < tokens.size() && tokens.get(currentIndex + getIndentations(tokens.get(currentIndex).getLine())).getType() == TokenType.KEYWORD && (tokens.get(currentIndex + getIndentations(tokens.get(currentIndex).getLine())).getValue().equals("else")))
        {
            currentIndex += getIndentations(tokens.get(currentIndex).getLine()); //end of if -> else
            currentIndex++; // else -> :
            if (isTokenNotColon()) new ParsingError("A colon (:) is expected after an if statement, but got " + tokens.get(currentIndex).getValue() + " of " + tokens.get(currentIndex).getType());
            currentIndex+=2; // : -> eol -> indentation
            while(currentIndex < tokens.size() && getIndentations(tokens.get(currentIndex).getLine()) > indentations_before_if)
            {
                currentIndex += getIndentations(tokens.get(currentIndex).getLine()); // skip indentations
                else_block.add(parseStatement());
                currentIndex++;
            }
            currentIndex--;
            return new If(arguments, then_block, else_block);
        }
        else return new If(arguments, then_block);

    }

    private static While parseWhile()
    {
        List<Token> condition = parseArguments(false);
        int indentations_before_while = getIndentations(tokens.get(currentIndex).getLine());
        List<Statement> body = new ArrayList<>();
        currentIndex++; // ) -> :
        if (isTokenNotColon()) throw new RuntimeException("A colon (:) is expected after a while statement");
        currentIndex+=2; // : -> eol -> indentation
        while(currentIndex < tokens.size() && getIndentations(tokens.get(currentIndex).getLine()) > indentations_before_while)
        {
            currentIndex += getIndentations(tokens.get(currentIndex).getLine()); // skip indentations
            body.add(parseStatement());
            currentIndex++;
        }
        currentIndex--;

        return new While(condition, body);
    }

    private static boolean isTokenNotColon()
    {
        return tokens.get(currentIndex).getType() != TokenType.SYMBOL || !tokens.get(currentIndex).getValue().equals(":");
    }
    private static boolean isTokenEOLorEOF()
    {
        return tokens.get(currentIndex).getType() == TokenType.EOL && tokens.get(currentIndex).getType() == TokenType.EOF;
    }

    private static List<Token> parseTableArguments()
    {
        List<Token> expression_tokens = new ArrayList<>();

        while(!(tokens.get(currentIndex).getType() == TokenType.SYMBOL && tokens.get(currentIndex).getValue().equals("]")))
        {
            expression_tokens.add(tokens.get(currentIndex));
            currentIndex++;
        }
        return expression_tokens;
    }
}
