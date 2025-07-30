package my.consler.catlanguage.ast.types;

import my.consler.catlanguage.ast.AstNode;

public class Identifier extends AstNode
{
    private final String name;

    public Identifier(String name)
    {
        this.name = name;

    }

    public String getName()
    {
        return name;

    }
}
