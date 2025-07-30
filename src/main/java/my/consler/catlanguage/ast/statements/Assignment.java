package my.consler.catlanguage.ast.statements;

import my.consler.catlanguage.ast.AstNode;

class Assignment extends Statement
{
    private final String variable;
    private final AstNode value;

    public Assignment(String variable, AstNode value)
    {
        this.variable = variable;
        this.value = value;

    }

    public String getVariable()
    {
        return variable;

    }

    public AstNode getValue()
    {
        return value;

    }
}
