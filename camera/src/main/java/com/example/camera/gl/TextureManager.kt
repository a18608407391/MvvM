package com.example.camera.gl

import android.graphics.SurfaceTexture


/**
 * openGl 工具类
 * */
class TextureManager{
    private var mSurfaceTexture: SurfaceTexture? = null
    fun getSurfaceTexture(): SurfaceTexture? {
      return mSurfaceTexture
    }

    /**
 *
float  bool  int    基本数据类型
vec2                包含了2个浮点数的向量
vec3                包含了3个浮点数的向量
vec4                包含了4个浮点数的向量
ivec2               包含了2个整数的向量
ivec3               包含了3个整数的向量
ivec4               包含了4个整数的向量
bvec2               包含了2个布尔数的向量
bvec3               包含了3个布尔数的向量
bvec4               包含了4个布尔数的向量
mat2                2*2维矩阵
mat3                3*3维矩阵
mat4                4*4维矩阵
sampler1D           1D纹理采样器
sampler2D           2D纹理采样器
sampler3D           3D纹理采样器
 * */



    constructor()

}