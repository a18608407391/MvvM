package com.zl.zlibrary.Utils

import kotlin.math.pow


/**
 * @param poss      贝塞尔曲线控制点坐标
 * @param precision 精度，需要计算的该条贝塞尔曲线上的点的数目
 * @return 该条贝塞尔曲线上的点（二维坐标）
 */
fun BezierCalculate(poss: Array<FloatArray>, precision: Int): Array<FloatArray>? {

    //维度，坐标轴数（二维坐标，三维坐标...）
    val dimersion = poss[0].size

    //贝塞尔曲线控制点数（阶数）
    val number = poss.size

    //控制点数不小于 2 ，至少为二维坐标系
    if (number < 2 || dimersion < 2)
        return null

    val result = Array(precision) { FloatArray(dimersion) }

    //计算杨辉三角
    val mi = IntArray(number)
    mi[1] = 1
    mi[0] = mi[1]
    for (i in 3..number) {
        val t = IntArray(i - 1)
        for (j in t.indices) {
            t[j] = mi[j]
        }
        mi[i - 1] = 1
        mi[0] = mi[i - 1]
        for (j in 0 until i - 2) {
            mi[j + 1] = t[j] + t[j + 1]
        }
    }

    //计算坐标点
    for (i in 0 until precision) {
        val t = i.toFloat() / precision
        for (j in 0 until dimersion) {
            var temp = 0.0f
            for (k in 0 until number) {
                temp += (Math.pow(
                    (1 - t).toDouble(),
                    (number - k - 1).toDouble()
                ) * poss[k][j].toDouble() * t.toDouble().pow(k.toDouble()) * mi[k].toDouble()).toFloat()
            }
            result[i][j] = temp
        }
    }

    return result
}