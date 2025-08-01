package consler.catlanguage.ast.statements.containers;

import consler.catlanguage.ast.expressions.Expression;
import consler.catlanguage.ast.statements.Statement;

import java.util.List;

public class If extends Statement {
    private final Expression condition;
    private final List<Statement> thenBlock;
    private final List<Statement> elseBlock; // Can be null or empty if no else clause

    public If(Expression condition, List<Statement> thenBlock, List<Statement> elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    // Getters
    public Expression getCondition()
    {
        return condition;

    }
    public List<Statement> getThenBlock()
    {
        return thenBlock;
    }
    public List<Statement> getElseBlock()
    {
        return elseBlock;

    }
}