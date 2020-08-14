package com.zl.zlibrary.Utils.memory;

public class BooleanClause {
    public static enum Occur {

        /** Use this operator for clauses that <i>must</i> appear in the matching documents. */
        MUST     { @Override public String toString() { return "+"; } },

        /** Like {@link #MUST} except that these clauses do not participate in scoring. */
        FILTER   { @Override public String toString() { return "#"; } },

        /** Use this operator for clauses that <i>should</i> appear in the
         * matching documents. For a BooleanQuery with no <code>MUST</code>
         * clauses one or more <code>SHOULD</code> clauses must match a document
         * for the BooleanQuery to match.
         * @see BooleanQuery.Builder#setMinimumNumberShouldMatch
         */
        SHOULD   { @Override public String toString() { return "";  } },

        /** Use this operator for clauses that <i>must not</i> appear in the matching documents.
         * Note that it is not possible to search for queries that only consist
         * of a <code>MUST_NOT</code> clause. These clauses do not contribute to the
         * score of documents. */
        MUST_NOT { @Override public String toString() { return "-"; } };

    }
}
