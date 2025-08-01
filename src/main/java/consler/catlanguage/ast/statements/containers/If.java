package consler.catlanguage.ast.statements.containers;

import consler.catlanguage.ast.statements.Statement;
import consler.catlanguage.lexer.token.Token;

import java.util.List;

public class If extends Statement
{
    private final List<Token> condition;
    private final List<Statement> thenBlock;
    private List<Statement> elseBlock = null; // Can be null or empty if no else clause

    public If(List<Token> condition, List<Statement> thenBlock)
    {
        this.condition = condition;
        this.thenBlock = thenBlock;
        //this.elseBlock = elseBlock;

    }

    public If(List<Token> condition, List<Statement> thenBlock, List<Statement> elseBlock)
    {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;

    }

    public List<Token> getCondition()
    {
        return condition;

    }
    public List<Statement> getThenBlock()
    {
        return thenBlock;

    }

    @Override
    public String toString()
    {
        return "\n if(" + condition.toString() + "){ \n " + thenBlock.toString() + "endIf}";
    }
    public List<Statement> getElseBlock()
    {
        return elseBlock;

    }
}