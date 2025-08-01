package consler.catlanguage.execution.execute.calls;

import consler.catlanguage.ast.statements.calls.Log;
import consler.catlanguage.parser.ParseExpression;

public class ExecuteLog
{
    public static void execute(Log log)
    {
        Object message =  ParseExpression.parse(log.getExpression());
        System.out.println(message);

    }

}
