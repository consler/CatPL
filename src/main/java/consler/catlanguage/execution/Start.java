package consler.catlanguage.execution;

import consler.catlanguage.ast.AstNode;
import consler.catlanguage.execution.execute.Execute;

import java.util.List;

public class Start
{
    public static void start(List<AstNode> events)
    {
        for (AstNode event : events)
        {
            Execute.event(event);

        }

    }

}
