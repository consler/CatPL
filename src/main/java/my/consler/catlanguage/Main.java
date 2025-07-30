package my.consler.catlanguage;

import my.consler.catlanguage.lexer.Lexer;
import my.consler.catlanguage.parser.Parser;
import my.consler.catlanguage.token.Token;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        String input =
"""
onStart:
    x = 12 + 4
    log(x)
    g
    
""";

        List<Token> tokens = Lexer.tokenize(input);
        //System.out.println(tokens);
        Parser.parse(tokens);

    }

}