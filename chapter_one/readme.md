初识OpenGL ES 2.0
=======
本章用来记录我学习的OpenGL Es 的第一章的知识点

***


|Author|写代码的向日葵|
|---|---|
|E-mail|1052105484@qq.com|

***

## 目录
### 第一章 初识 OpenGL ES 2.0
* 1.1 OpenGL Es2.0概览
  * 1.1.1 OpenGL Es2.0 简介
  * 1.1.2 初识OpenGL Es2.0 应用程序
* 1.2 着色器与渲染管线
  * 1.2.1 OpenGL Es 1.x 的渲染管线
  * 1.2.2 OpenGL ES 2.0 渲染管线
  * 1.2.3 OpenGL Es 中立体物体的构建
  
*****
    
# 1.1 OpenGl ES 2.0概述
* 随着3G时代的到来，Andriod与iPhone逐渐成为消费者购买智能手机的主要选择。而由于基于Android的智能手机性能优良、价格合适，因此Android智能手机得到了大多数用户的青睐。
* 随着Android系统版本及硬件水平的提升，OpenGL Es版本也由原先仅支持固定渲染管线的OpenGL Es 1.x 升级为支持自定义渲染管线的OpenGl Es2.0 。这使得使用OpenGL Es 2.0 渲染的3D场景更加真实，从而能够创造全新的用户体验。
        
### 1.1.1 OpenGL ES 2.0简介
* 现今较为知名的3D图形API有OpenGL、DirectX以及OpenGL Es,他们各自的应用领域如下。

    1 . DirectX 主要应用于Windows下游戏的开发，再次领域基本上一统天下
   
    2 . OpenGL 的应用领域较为广泛，适用于Unix、Mac Os、Linux以及Microsoft等几乎所有的操作系统，可以开发游戏、工业建模以及嵌入式设备。
   
    3 . OpenGl Es 是专门针对于嵌入式设备而设计的，其实际是OpenGL 的裁剪版本，去除了OpenGL中许多不是必须存在的特性，如:GL_QUADS(四边形)与GL_POLYGONS(多边形)绘制模式以及glBegin（开始）glEnd(结束)操作等。

* 经过多年的发展，OpenGL Es 主要分为两个主版本，其基本情况如下。

    1 . 一个是OpenGL Es 1.x（主要包括1.0与1.1），其采用的是固定渲染管线，可以由硬件GPU支持或用软件模式实现，渲染能力有限，在纯软件模拟情况下性能也较弱。（典型的使用OpenGL ES 1.x渲染技术的游戏：都市赛车5）
    
    2 . 另一个是OpenGL ES 2.0,其采用的是可编程渲染管线，渲染能力大大提高。`OpenGL ES 2.0`  要求设备中必须有相应的GPU硬件设备的支持，目前不支持在设备上用软件模拟实现。（典型的使用OpenGL ES 2.0 渲染技术的游戏：都市赛车6）
    
***
# 1.1.2 初识OPenGL ES 2.0 应用程序
 * ShaderUtil.java
 
````java
import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by zhangqing on 2017/12/3.
 * 加载顶点与片元着色器的类
 */

public final class ShaderUtil {
    private static final String TAG = "ES20_ERROR";

    /**
     * 加载指定着色器的方法
     *
     * @param shaderType:着色器的类型: GLES20.GL_VERTEX_SHADER：顶点着色器
     *                           GLES20.GL_FRAGMENT_SHADER：片元着色器
     * @param source:着色器的脚本字符串
     * @return
     */
    public static int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);//创建个shader，并且记录其id
        if (shader != 0)//若创建成功，则加载着色器
        {
            GLES20.glShaderSource(shader, source);//加载着色器的源代码
            GLES20.glCompileShader(shader);//编译
            int[] compiled = new int[1];
            //获取shader的编译情况
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0)//如果编译失败则显示错误日志并且删除此shader
            {
                Log.e(TAG, "Could not compile shader " + shaderType + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    /**
     * 检查每一步操作是否有错误的方法
     *
     * @param op
     */
    public static void checkGLError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ":glError" + error);
            throw new RuntimeException(op + ":glError" + error);
        }
    }

    /**
     * 从sh脚本中加载着色器内容的方法
     *
     * @param fname     :Assets中着色器脚本文件的名称
     * @param resources :资源引用
     * @return
     */
    public static String loadFromAssetsFile(String fname, Resources resources) {
        String result = null;
        try {
            InputStream inputStream = resources.getAssets().open(fname);//从Assets文件中读取信息
            int ch = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((ch = inputStream.read()) != -1) {
                baos.write(ch);//将获取的信息写入输出流中
            }
            byte[] buffer = baos.toByteArray();
            baos.close();   //关闭输出流
            inputStream.close();
            result = new String(buffer, "UTF-8"); //转化为UTF-8编码
            result = result.replaceAll("\\r\\n", "\n");
        } catch (Exception e) {//捕获异常
            e.printStackTrace();//打印异常
        }
        return result;//返回结果
    }

    /**
     * 创建着色器程序的方法
     *
     * @param vertexSource 
     * @param fragmentSource
     * @return
     */
    public static int createProgram(String vertexSource, String fragmentSource) {
        //加载顶点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        //加载片元着色器
        int pixeShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixeShader == 0) {
            return 0;
        }
        //创建程序
        int program = GLES20.glCreateProgram();
        if (program != 0)//若程序创建成功则向程序中加入顶点着色器与片元着色器
        {
            GLES20.glAttachShader(program, vertexShader);//向程序中加入顶点着色器
            checkGLError("glAttachShader");
            GLES20.glAttachShader(program, pixeShader);//向程序中加入片元着色器
            checkGLError("glAttachShader");
            GLES20.glLinkProgram(program);//链接程序
            int[] linkStatus = new int[1];//存放链接成功program状态值的数组
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE)//若链接失败则报错并删除程序
            {
                Log.e(TAG, "Could not link program:");
                Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }
}

    
