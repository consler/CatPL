package consler.catlanguage.execution.execute;

import consler.catlanguage.ast.statements.Assignment;
import consler.catlanguage.ast.statements.Statement;
import consler.catlanguage.ast.statements.calls.Log;
import consler.catlanguage.execution.execute.assignment.ExecuteAssigment;
import consler.catlanguage.execution.execute.calls.ExecuteLog;

public class Execute
{
    public static void statement(Statement statement)
    {
        if(statement instanceof Log)
        {
            ExecuteLog.execute(((Log) statement));
        }
        else if(statement instanceof Assignment)
        {
            ExecuteAssigment.execute((Assignment) statement);
        }

    }

}
