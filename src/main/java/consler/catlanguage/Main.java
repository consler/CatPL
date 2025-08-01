package consler.catlanguage;

import consler.catlanguage.lexer.Lexer;
import consler.catlanguage.parser.Parser;
import consler.catlanguage.token.Token;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        String input =
"""
onStart:
    x = 12
    log(x)
    log("yoo" + 12)
""";

        List<Token> tokens = Lexer.tokenize(input);
        //System.out.println(tokens);
        Parser.parse(tokens);

    }

}