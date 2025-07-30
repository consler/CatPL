package my.consler.catlanguage.ast.statements;

import my.consler.catlanguage.ast.AstNode;

import java.util.List;

class If extends Statement
{
    private final AstNode condition;
    private final List<Statement> thenStatements;
    private final List<Statement> elseStatements;

    public If(AstNode condition, List<Statement> thenStatements, List<Statement> elseStatements)
    {
        this.condition = condition;
        this.thenStatements = thenStatements;
        this.elseStatements = elseStatements;

    }

    public AstNode getCondition()
    {
        return condition;

    }

    public List<Statement> getThenStatements()
    {
        return thenStatements;

    }

    public List<Statement> getElseStatements()
    {
        return elseStatements;

    }
}
