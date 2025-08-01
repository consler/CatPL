package consler.catlanguage.ast.types;

import consler.catlanguage.ast.AstNode;

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
