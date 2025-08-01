package consler.catlanguage.ast.events;

import consler.catlanguage.ast.AstNode;
import consler.catlanguage.ast.statements.Statement;

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