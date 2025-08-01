package consler.catlanguage.ast.statements.calls;

import consler.catlanguage.ast.statements.Call;
import consler.catlanguage.lexer.token.Token;

import java.util.List;

public class Log extends Call
{
    private final List<Token> expression;

    public Log(List<Token> expression)
    {
        this.expression = expression;

    }

    public List<Token> getExpression()
    {
        return expression;

    }

    @Override
    public String toString()
    {

        return "\n Log: " + expression.toString();

    }

}
