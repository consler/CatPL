package consler.catlanguage;

import consler.catlanguage.ast.AstNode;
import consler.catlanguage.execution.Start;
import consler.catlanguage.lexer.Lexer;
import consler.catlanguage.parser.Parser;
import consler.catlanguage.lexer.token.Token;

import java.io.*;
import java.util.List;


public class Main
{
    public static boolean test = true;
    public static boolean debug = false;

    public static void main(String[] args)
    {
        if(args.length > 1)
        {
            System.err.println("Incorrect usage. Usage - catpl.jar file.cpl");
            System.exit(1);
        }

        StringBuilder input = new StringBuilder();

        if(test)
        {
            input = new StringBuilder(
"""
onStart:
    i=0
    while(i<100):
        if(i==99):
            log("!!!")
        else:
            log(i)
        i = i +1
    log("i is " + i)
"""
            );
        }
        else
        {
            try
            {
                FileReader fr = new FileReader(args[0]);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null)
                {
                    input.append(line).append("\n");
                }
                br.close();
            }
            catch (FileNotFoundException e)
            {
                System.err.println("File not found: " + args[0]);
                System.exit(1);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        List<Token> tokens = Lexer.tokenize(input.toString());
        List<AstNode> ast = Parser.parse(tokens);
        Start.start(ast);

    }

}