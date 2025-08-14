package consler.catlanguage.execution.execute.calls;

import consler.catlanguage.ast.statements.calls.Print;
import consler.catlanguage.parser.ParseExpression;

public class ExecutePrint
{
    public static void execute(Print print)
    {
        Object message = ParseExpression.parse(print.getExpression());
        System.out.println(message);
    }
}
