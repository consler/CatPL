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
    public static boolean debug = true;

    public static void main(String[] args)
    {
        if(args.length > 1)
        {
            System.err.println("Incorrect usage. Usage - catpl.jar -jar file.cpl");
            System.exit(1);
        }

        StringBuilder input = new StringBuilder();

        if(test)
        {
            input = new StringBuilder(
"""
onStart("code below runs on start"):
    table[12 ] = "hejsan"
    print(table[12])
""");
        }
        else
        {
            try
            {
                FileReader fr = new FileReader(args[0]);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null)
                    input.append(line).append("\n");

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