package my.consler.catlanguage.ast.statements;

import my.consler.catlanguage.ast.AstNode;

import java.util.ArrayList;
import java.util.List;

public class Call extends Statement
{
    private final String callee;
    private final List<AstNode> arguments;

    public Call(String callee, List<AstNode> arguments)
    {
        this.callee = callee;
        this.arguments = arguments;

    }

    public String getCallee()
    {
        return callee;

    }

    public List<AstNode> getArguments()
    {
        return arguments;

    }

}
