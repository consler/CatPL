package consler.catlanguage.parser;

import consler.catlanguage.Main;

public class ParsingError
{
    public ParsingError(String reason)
    {
        System.err.println("Parsing error at line " + Parser.tokens.get(Parser.currentIndex).getLine() +  ": " + reason);
        if(Main.test) throw new RuntimeException();
        else System.exit(1);
    }
}
