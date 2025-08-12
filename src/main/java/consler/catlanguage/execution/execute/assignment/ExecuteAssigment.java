package consler.catlanguage.execution.execute.assignment;

import consler.catlanguage.ast.statements.Assignment;
import consler.catlanguage.parser.ParseExpression;

import static consler.catlanguage.Main.debug;

public class ExecuteAssigment
{
    public static void execute(Assignment assignment)
    {
        if(debug) System.out.println(assignment);
        Variable.setVariable(assignment.getVariable(), ParseExpression.parse(assignment.getValue()));

    }
}
