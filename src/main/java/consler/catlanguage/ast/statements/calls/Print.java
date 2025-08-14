package consler.catlanguage.ast.statements.calls;

import consler.catlanguage.ast.statements.Call;
import consler.catlanguage.lexer.token.Token;

import java.util.List;

public class Print extends Call
{
    private final List<Token> expression;

    public Print(List<Token> expression)
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
        return "\n Print: " + expression.toString();
    }
}
