package consler.catlanguage.execution.execute.assignment;

import consler.catlanguage.ast.statements.Assignment;
import consler.catlanguage.parser.ParseExpression;

public class ExecuteAssigment
{
    public static void execute(Assignment assignment)
    {
        Variable.setVariable(assignment.getVariable(), ParseExpression.parse(assignment.getValue()));

    }
}
