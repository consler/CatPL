package my.consler.catlanguage.ast.types;

import my.consler.catlanguage.ast.AstNode;

public class String extends AstNode
{
    private final String value;

    public String(String value)
    {
        this.value = value;

    }

    public String getValue()
    {
        return value;

    }

}
