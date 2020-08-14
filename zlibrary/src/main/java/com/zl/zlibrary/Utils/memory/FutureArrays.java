package com.zl.zlibrary.Utils.memory;

class FutureArrays {
    private static void checkFromToIndex(int fromIndex, int toIndex, int length) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex " + fromIndex + " > toIndex " + toIndex);
        }
        if (fromIndex < 0 || toIndex > length) {
            throw new IndexOutOfBoundsException("Range [" + fromIndex + ", " + toIndex + ") out-of-bounds for length " + length);
        }
    }
    public static int compareUnsigned(byte[] a, int aFromIndex, int aToIndex, byte[] b, int bFromIndex, int bToIndex) {
        checkFromToIndex(aFromIndex, aToIndex, a.length);
        checkFromToIndex(bFromIndex, bToIndex, b.length);
        int aLen = aToIndex - aFromIndex;
        int bLen = bToIndex - bFromIndex;
        int len = Math.min(aLen, bLen);
        for (int i = 0; i < len; i++) {
            int aByte = a[i+aFromIndex] & 0xFF;
            int bByte = b[i+bFromIndex] & 0xFF;
            int diff = aByte - bByte;
            if (diff != 0) {
                return diff;
            }
        }

        // One is a prefix of the other, or, they are equal:
        return aLen - bLen;
    }

}
