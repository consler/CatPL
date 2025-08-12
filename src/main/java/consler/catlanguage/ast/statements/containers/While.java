package consler.catlanguage.ast.statements.containers;

import consler.catlanguage.ast.statements.Statement;
import consler.catlanguage.lexer.token.Token;

import java.util.List;

public class While extends Statement
{
    private final List<Token> condition;
    private final List<Statement> body;

    public While(List<Token> condition, List<Statement> body)
    {
        this.condition = condition;
        this.body = body;

    }
    public List<Token> getCondition()
    {
        return condition;

    }
    public List<Statement> getBody()
    {
        return body;

    }

    @Override
    public String toString()
    {
        return "While(" + getCondition() + "){\n" + getBody() + "\n}";

    }

}