package com.zl.zlibrary.Utils.memory;

public class BytesRef implements Comparable<BytesRef>, Cloneable{
    /** An empty byte array for convenience */
    /** The contents of the BytesRef. Should never be {@code null}. */
    public byte[] bytes;

    /** Offset of first valid byte. */
    public int offset;

    /** Length of used bytes. */
    public int length;


    /** Unsigned byte order comparison */
    @Override
    public int compareTo(BytesRef other) {
        return FutureArrays.compareUnsigned(this.bytes, this.offset, this.offset + this.length,
                other.bytes, other.offset, other.offset + other.length);
    }
}
