package com.zl.zlibrary.Utils.memory;

public abstract class Query {
    public void visit(QueryVisitor visitor) {
        visitor.visitLeaf(this);
    }
}
