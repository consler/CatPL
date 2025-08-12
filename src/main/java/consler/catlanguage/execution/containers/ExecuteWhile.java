package consler.catlanguage.execution.containers;

import consler.catlanguage.ast.statements.Statement;
import consler.catlanguage.ast.statements.containers.While;
import consler.catlanguage.execution.execute.Execute;
import consler.catlanguage.parser.ParseExpression;

public class ExecuteWhile
{
    public static void execute(While while_block)
    {
        while( ParseExpression.parseCondition(while_block.getCondition()))
        {
            for(Statement statement : while_block.getBody())
            {
                Execute.statement(statement);
            }
        }
    }
}
