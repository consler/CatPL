package consler.catlanguage;

import consler.catlanguage.ast.AstNode;
import consler.catlanguage.execution.Start;
import consler.catlanguage.lexer.Lexer;
import consler.catlanguage.parser.Parser;
import consler.catlanguage.lexer.token.Token;

import java.util.List;


public class Main
{
    public static boolean debug = false;

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
    i=1
    i = i+1
    while(i<10):
        if(i==5):
            log("i is " + i)
        else:
            log(i)
        i = i +1
    log("i is " + i)


""";

        List<Token> tokens = Lexer.tokenize(input);
        List<AstNode> ast = Parser.parse(tokens);
        Start.start(ast);


    }

}