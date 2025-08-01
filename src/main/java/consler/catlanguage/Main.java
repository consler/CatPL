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
    x = 7
    y = 12 + 4/2
    log("hmm what could be the answer??")

onStart:
    z=10/5
    log("not "+ z)
    answer = 1
    if(answer == 2):
        log("answers 1")
    else:
        log("answers not 1")

""";

        List<Token> tokens = Lexer.tokenize(input);
        List<AstNode> ast = Parser.parse(tokens);
        //System.out.println(ast);
        Start.start(ast);


    }

}