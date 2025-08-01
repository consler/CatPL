package consler.catlanguage.execution;

import consler.catlanguage.ast.AstNode;
import consler.catlanguage.ast.events.OnStart;
import consler.catlanguage.ast.statements.Statement;
import consler.catlanguage.execution.execute.Execute;

import java.util.List;

public class Start
{
    public static void start(List<AstNode> events)
    {
        for (AstNode event : events)
        {
            if (event instanceof OnStart)
            {
                for (Statement statement : ((OnStart) event).getStatements())
                {
                    Execute.statement(statement);
                }

            }

        }

    }

}
