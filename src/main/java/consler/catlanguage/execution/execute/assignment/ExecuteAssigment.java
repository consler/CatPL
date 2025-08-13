package consler.catlanguage.execution.execute.assignment;

import consler.catlanguage.ast.statements.Assignment;
import consler.catlanguage.parser.ParseExpression;

import static consler.catlanguage.Main.debug;

public class ExecuteAssigment
{
    public static void execute(Assignment assignment)
    {
        if(assignment.getIndex() == null)
            variable(assignment);
        else
            table(assignment);
    }
    public static void variable(Assignment assignment)
    {
        if(debug) System.out.println(assignment);
        Value.setIdentifier(assignment.getIdentifier(), ParseExpression.parse(assignment.getValue()));
    }
    public static void table(Assignment assignment)
    {
        if(debug) System.out.println(assignment);
        Value.setIdentifier(assignment.getIdentifier(), ParseExpression.parse( assignment.getIndex()), ParseExpression.parse(assignment.getValue()));
    }
}
