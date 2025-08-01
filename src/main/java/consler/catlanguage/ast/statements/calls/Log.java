package consler.catlanguage.ast.statements.calls;

import consler.catlanguage.ast.statements.Call;
import consler.catlanguage.token.Token;

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
        StringBuilder sb = new StringBuilder();
        sb.append("Log: ");
        for (Token token : expression)
        {
            sb.append(token.toString());
            sb.append(",");
        }

        return sb.toString();

    }

}
