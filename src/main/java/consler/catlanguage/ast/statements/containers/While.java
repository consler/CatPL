package consler.catlanguage.ast.statements.containers;

import consler.catlanguage.ast.expressions.Expression;
import consler.catlanguage.ast.statements.Statement;

import java.util.List;

public class While extends Statement
{
    private final Expression condition;
    private final List<Statement> body;

    public While(Expression condition, List<Statement> body)
    {
        this.condition = condition;
        this.body = body;

    }
    public Expression getCondition()
    {
        return condition;

    }
    public List<Statement> getBody()
    {
        return body;

    }

}