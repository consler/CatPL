package consler.catlanguage.execution.containers;

import consler.catlanguage.ast.statements.Statement;
import consler.catlanguage.ast.statements.containers.If;
import consler.catlanguage.execution.execute.Execute;
import consler.catlanguage.parser.ParseExpression;

public class ExecuteIf
{
    public static void execute(If ifStatement)
    {
        if(ParseExpression.parseCondition(ifStatement.getCondition()))
        {
            for(Statement statement : ifStatement.getThenBlock())
            {
                Execute.statement(statement);
            }
        }
        else if (ifStatement.getElseBlock() != null)
        {
            for(Statement statement : ifStatement.getElseBlock())
            {
                Execute.statement(statement);
            }
        }
    }
}
