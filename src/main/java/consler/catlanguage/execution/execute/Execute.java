package consler.catlanguage.execution.execute;

import consler.catlanguage.ast.AstNode;
import consler.catlanguage.ast.events.OnStart;
import consler.catlanguage.ast.statements.Assignment;
import consler.catlanguage.ast.statements.Statement;
import consler.catlanguage.ast.statements.calls.Log;
import consler.catlanguage.ast.statements.containers.If;
import consler.catlanguage.ast.statements.containers.While;
import consler.catlanguage.execution.containers.ExecuteIf;
import consler.catlanguage.execution.containers.ExecuteWhile;
import consler.catlanguage.execution.execute.assignment.ExecuteAssigment;
import consler.catlanguage.execution.execute.calls.ExecuteLog;

public class Execute
{
    public static void event(AstNode event)
    {
        if (event instanceof OnStart)
        {
            for (Statement statement : ((OnStart) event).getStatements())
            {
                Execute.statement(statement);
            }
        }
    }

    public static void statement(Statement statement)
    {
        if(statement instanceof Log) ExecuteLog.execute(((Log) statement));
        else if(statement instanceof Assignment) ExecuteAssigment.execute((Assignment) statement);
        else if (statement instanceof If) ExecuteIf.execute((If) statement);
        else if(statement instanceof While) ExecuteWhile.execute((While) statement);
    }

}
