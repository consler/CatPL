package consler.catlanguage.ast.types;

import consler.catlanguage.ast.AstNode;

public class LiteralString extends AstNode
{
    private final LiteralString value;

    public LiteralString(LiteralString value)
    {
        this.value = value;

    }

    public LiteralString getValue()
    {
        return value;

    }

}
