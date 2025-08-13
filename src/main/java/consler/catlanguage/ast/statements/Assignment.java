package consler.catlanguage.ast.statements;

import consler.catlanguage.lexer.token.Token;

import java.util.List;

public class Assignment extends Statement
{
    private final String identifier;
    private final List<Token> value;
    private List<Token> index = null;

    public Assignment(String identifier, List<Token> value)
    {
        this.identifier = identifier;
        this.value = value;
    }

    public Assignment(String identifier, List<Token> index, List<Token> value)
    {
        this.identifier = identifier;
        this.value = value;
        this.index = index;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public List<Token> getValue()
    {
        return value;
    }

    public List<Token> getIndex()
    {
        return index;
    }

    @Override
    public String toString()
    {
        if (index == null)
            return "Assignment: " + identifier + " = " + value;
        else
            return "Assignment: " + identifier + "[" + index + "] = " + value;
    }
}
