package my.consler.catlanguage.ast;

public class Expression extends AstNode
{
    private final String operator;
    private final AstNode left;
    private final AstNode right;

    public Expression(String operator, AstNode left, AstNode right)
    {
        this.operator = operator;
        this.left = left;
        this.right = right;

    }

    public String getOperator()
    {
        return operator;

    }

    public AstNode getLeft()
    {
        return left;

    }

    public AstNode getRight()
    {
        return right;

    }
}
