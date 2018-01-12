package triangle.shape;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.Matrix;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import triangle.utils.ShaderUtil;

/**
 * Created by zhangqing on 2017/12/6.
 */

public class Triangle {
    public static float[] mProjMatrix = new float[16];//4x4投影矩阵
    public static float[] mVMatrix = new float[16];//摄像机位置参数朝向矩阵
    public static float[] mMVPMatrix;//总变换矩阵
    int mProgram;//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int maPositionHandle;//顶点位置属性引用
    int maColorHandle;//顶点颜色属性引用
    String mVertexShader;//顶点着色器代码脚本
    String mFragmentShader;//片元着色器代码脚本
    static float[] mMMatrix = new float[16];//具体物体的3D变换矩阵，包括旋转，平移，缩放


    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    FloatBuffer mColorBuffer;//顶点颜色数据缓冲
    int vCount = 0;//顶点数量
    public float xAngle = 0;//绕X轴旋转的角度

    public Triangle(Context context)//构造器
    {
        initVertexData();//调用初始化顶点数据的initVertexData方法
        initShader(context.getResources());//调用初始化着色器的initShader方法
    }

    public void initVertexData()//自定义的初始化顶点数据的方法
    {
        vCount = 3;                   //顶点数量为3
        final float UNIT_SIZE = 0.5F;//设置单位长度
        float vertices[] = new float[]{//设置顶点坐标数组

                -4 * UNIT_SIZE, 0, 0,
                0, -4 * UNIT_SIZE, 0,
                4 * UNIT_SIZE, 0, 0
        };
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序为本地操作系统顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为浮点(Float)型缓冲
        mVertexBuffer.put(vertices);//在缓冲区中写入数据
        mVertexBuffer.position(0);//设置缓冲区起始位置

        float colors[] = new float[]{//顶点颜色数组

                1, 1, 1, 0,
                0, 0, 1, 0,
                0, 1, 0, 0
        };

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    /**
     * 创建并初始化着色器的方法
     * @param resources
     */
    public void initShader(Resources resources) {
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh", resources);
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh", resources);
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf() {
        GLES20.glUseProgram(mProgram);
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);//初始化变换矩阵
        Matrix.translateM(mMMatrix, 0, 0, 0, -0.5f);//设置延Z轴负向平移
        Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);//设置绕X轴旋转
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, getFinalMatrix(mMMatrix), 0);
        GLES20.glVertexAttribPointer(maPositionHandle, vCount, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);//将顶点位置数据传送进渲染管线
        GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false, 4 * 4, mColorBuffer);//将顶点颜色数据传送进渲染管线
        GLES20.glEnableVertexAttribArray(maPositionHandle);//启用顶点位置数据
        GLES20.glEnableVertexAttribArray(maColorHandle);//启用顶点着色数据
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);//执行绘制
    }

    /**
     * 产生最终变换矩阵的方法
     * @param spec
     * @return
     */
    public static float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16];//初始化总变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;//返回总变换矩阵
    }
}
