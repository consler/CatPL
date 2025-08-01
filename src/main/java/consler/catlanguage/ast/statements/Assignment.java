package consler.catlanguage.ast.statements;

import consler.catlanguage.token.Token;

import java.util.List;

public class Assignment extends Statement
{
    private final String variable;
    private final List<Token> value;

    public Assignment(String variable, List<Token> value)
    {
        this.variable = variable;
        this.value = value;

    }

    public String getVariable()
    {
        return variable;

    }

    public List<Token> getValue()
    {
        return value;

    }

    @Override
    public String toString()
    {
        return "Assignment: " + variable + " = " + value;

    }
}
