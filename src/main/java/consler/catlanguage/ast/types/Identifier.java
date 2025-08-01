package consler.catlanguage.ast.types;

import consler.catlanguage.ast.AstNode;
import consler.catlanguage.execution.execute.assignment.Variable;

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

    public Object getValue()
    {
        return Variable.getVariable(name);

    }
}
