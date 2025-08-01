package consler.catlanguage.ast.statements.containers;

import consler.catlanguage.ast.statements.Statement;
import consler.catlanguage.lexer.token.Token;

import java.util.List;

public class If extends Statement
{
    private final List<Token> condition;
    private final List<Statement> thenBlock;
    //private final List<Statement> elseBlock; // Can be null or empty if no else clause

    public If(List<Token> condition, List<Statement> thenBlock)
    {
        this.condition = condition;
        this.thenBlock = thenBlock;
        //this.elseBlock = elseBlock;

    }

    public List<Token> getCondition()
    {
        return condition;

    }
    public List<Statement> getThenBlock()
    {
        return thenBlock;

    }
//    public List<Statement> getElseBlock()
//    {
//        return elseBlock;
//
//    }
}