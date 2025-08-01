package consler.catlanguage;

import consler.catlanguage.ast.AstNode;
import consler.catlanguage.execution.Start;
import consler.catlanguage.lexer.Lexer;
import consler.catlanguage.parser.Parser;
import consler.catlanguage.lexer.token.Token;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        String input =
"""
onStart:
    z=12
    log("yo")
    x = 12
    log(x)
    log("yoo" + 12)
    if(12 != 10):
        log("omg")
        log("hello")
    log("umm")

onStart:
    y= x +z
    log(y-1)
""";

        List<Token> tokens = Lexer.tokenize(input);
        List<AstNode> ast = Parser.parse(tokens);
        Start.start(ast);


    }

}