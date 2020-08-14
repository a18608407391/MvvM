package com.zl.zlibrary.Utils.memory;

import java.util.Collection;

public final class Term implements Comparable<Term>, Accountable{
    private static final long BASE_RAM_BYTES = RamUsageEstimator.shallowSizeOfInstance(Term.class) +
            RamUsageEstimator.shallowSizeOfInstance(BytesRef.class);
    String field;
    BytesRef bytes;

    @Override
    public long ramBytesUsed() {
        return BASE_RAM_BYTES +
                RamUsageEstimator.sizeOfObject(field) +
                (bytes != null ? RamUsageEstimator.alignObjectSize(bytes.bytes.length + RamUsageEstimator.NUM_BYTES_ARRAY_HEADER) : 0L);
    }

    @Override
    public Collection<Accountable> getChildResources() {
        return null;
    }

    @Override
    public int compareTo(Term other) {
        if (field.equals(other.field)) {
            return bytes.compareTo(other.bytes);
        } else {
            return field.compareTo(other.field);
        }
    }
}
