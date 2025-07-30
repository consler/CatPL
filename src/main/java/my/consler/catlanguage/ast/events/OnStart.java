package my.consler.catlanguage.ast.events;

import my.consler.catlanguage.ast.AstNode;
import my.consler.catlanguage.ast.statements.Statement;

import java.util.List;

public class OnStart extends AstNode
{
    private final List<Statement> statements;

    public OnStart(List<Statement> statements)
    {
        this.statements = statements;

    }

    public List<Statement> getStatements()
    {
        return statements;

    }
}