package consler.catlanguage.parser;

import consler.catlanguage.Main;

public class RuntimeError
{
    public RuntimeError(int line, String reason)
    {
        System.err.println(reason + " at line " + line);
        if(Main.test) throw new RuntimeException();
        else System.exit(1);
    }
}
