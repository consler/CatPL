package consler.catlanguage;

import consler.catlanguage.ast.AstNode;
import consler.catlanguage.execution.Start;
import consler.catlanguage.lexer.Lexer;
import consler.catlanguage.parser.Parser;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        String input =
"""
onStart:
    log("yo")
    x = 12
    log(x)
    log("yoo" + 12)

onStart:
    y=1
    log(y-1)
""";

        List<AstNode> ast = Parser.parse(Lexer.tokenize(input));
        Start.start(ast);


    }

}