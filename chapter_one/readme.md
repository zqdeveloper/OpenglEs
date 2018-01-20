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
* 1.1 OpenGL ES 2.0概览
  * 1.1.1 OpenGL ES 2.0 简介
  * 1.1.2 初识OpenGL ES 2.0 应用程序
* 1.2 着色器与渲染管线
  * 1.2.1 OpenGL ES 1.x 的渲染管线
  * 1.2.2 OpenGL ES 2.0 渲染管线
  * 1.2.3 OpenGL ES 中立体物体的构建
  
*****
    
# 1.1 OpenGl ES 2.0概述
* 随着3G时代的到来，Andriod与iPhone逐渐成为消费者购买智能手机的主要选择。而由于基于Android的智能手机性能优良、价格合适，因此Android智能手机得到了大多数用户的青睐。
* 随着Android系统版本及硬件水平的提升，OpenGL Es版本也由原先仅支持固定渲染管线的OpenGL Es 1.x 升级为支持自定义渲染管线的OpenGl ES 2.0 。这使得使用OpenGL ES 2.0 渲染的3D场景更加真实，从而能够创造全新的用户体验。
        
### 1.1.1 OpenGL ES 2.0简介
* 现今较为知名的3D图形API有OpenGL、DirectX以及OpenGL ES,他们各自的应用领域如下。
    1. DirectX 主要应用于Windows下游戏的开发，在此领域基本上一统天下
    2. OpenGL 的应用领域较为广泛，适用于Unix、Mac Os、Linux以及Microsoft等几乎所有的操作系统，可以开发游戏、工业建模以及嵌入式设备。
    3. OpenGl Es 是专门针对于嵌入式设备而设计的，其实际是OpenGL 的裁剪版本，去除了OpenGL中许多不是必须存在的特性，如:GL_QUADS(四边形)与GL_POLYGONS(多边形)绘制模式以及glBegin（开始）glEnd(结束)操作等。
* 经过多年的发展，OpenGL Es 主要分为两个主版本，其基本情况如下。
  1. 一个是OpenGL Es 1.x（主要包括1.0与1.1），其采用的是固定渲染管线，可以由硬件GPU支持或用软件模式实现，渲染能力有限，在纯软件模拟情况下性能也较弱。（典型的使用OpenGL ES 1.x渲染技术的游戏：都市赛车5）
  2. 另一个是OpenGL ES 2.0,其采用的是可编程渲染管线，渲染能力大大提高。`OpenGL ES 2.0`  要求设备中必须有相应的GPU硬件设备的支持，目前不支持在设备上用软件模拟实现。（典型的使用OpenGL ES 2.0 渲染技术的游戏：都市赛车6）
    
***
### 1.1.2 初识OPenGL ES 2.0 应用程序
 * ShaderUtil.java
 
```java
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
```

***


* Triangle.java
```java

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.opengl.utils.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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
```

***

* MyTDView.java
```java
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.opengl.shape.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zhangqing on 2017/12/6.
 */

public class MyTDView extends GLSurfaceView {
    //每次三角形旋转的角度
    final float ANGLE_SPAN = 0.5f;
    RotateThread rthread;
    SceneRenderer sceneRenderer;

    public MyTDView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        sceneRenderer = new SceneRenderer();
        this.setRenderer(sceneRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {

        Triangle triangle;//声明STriangle类的引用

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            triangle = new Triangle(getContext());
            GLES20.glClearColor(0, 0, 0, 1.0f);//设置屏幕背景色
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            rthread = new RotateThread();//创建RotateThread类的对象
            rthread.start();//开启线程
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);//设置视口
            float ratio = (float) width / height;//计算屏幕的宽度和高度比
            Matrix.frustumM(Triangle.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10);//设置透视投影
            Matrix.setLookAtM(Triangle.mVMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);//设置摄像机

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            triangle.drawSelf();
        }
    }

    public class RotateThread extends Thread//自定义的内部类线程
    {
        public boolean flag = true;//设置循环标志位

        @Override
        public void run() {
            while (flag) {
                sceneRenderer.triangle.xAngle = sceneRenderer.triangle.xAngle + ANGLE_SPAN;
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

***

* MainActivity.java
```java
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.opengl.view.MyTDView;

public class MainActivity extends AppCompatActivity {


    MyTDView mview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mview = new MyTDView(this);
        mview.requestFocus();//获取焦点
        mview.setFocusableInTouchMode(true);//设置为可触控
        setContentView(mview);//跳转到相关界面
    }

    @Override
    protected void onResume() {
        super.onResume();
        mview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mview.onPause();
    }
}
```

***
# 1.2 着色器与渲染管线
### 1.2.1 OpenGL Es 1.x的渲染管线
* 渲染管线也称之为渲染流水线，一般由显示芯片(GPU)内部处理图形信号的并行处理单元组成。这些并行处理单元两两之间是相互独立的，在不同型号的硬件上独立处理单元的数量也是很大的差异。一般越高端型号的硬件，其中独立处理单元的数量也就越多。
* 与普通应用程序通过GPU串行执行不同，将渲染工作通过渲染管线中多个相互独立的处理单元进行并行处理后，渲染效率可以得到极大的提升。
* OpenGL Es中的渲染管线实质上指的是一系列绘制过程。这些过程输入的是待渲染的3D物体的相关描述信息数据，经过渲染管线，输出的是一帧想要的图像，OpenGL Es 1.x的渲染管线如图所示：
 1. 基本处理
    * 该阶段设定3D空间中物体的顶点坐标，顶点对应的颜色、顶点的纹理坐标等属性，并且指定绘制方式，如:点绘制、线段绘制或者三角形绘制等
 2. 顶点缓冲对象
   	* 这部分功能在应用程序中是可选的，对于某些在整个场景中的顶点的基本数据不变的情况。可以在初始化阶段将顶点数据经过基本处理后送入顶点缓冲对象，在绘制每一帧想要的图像时就省去了顶点数据IO的麻烦，直接从缓冲对象中获取顶点数据即可。相比于每次绘制时单独将顶点数据送入GPU的方式，可以在一定程度上节省GPU的IO带宽，提高渲染效率。
 3. 变换和光照
    * 该阶段的工作主要是进行顶点变换以及根据程序中设置的光照属性对顶点进行光照计算。顶点变换的任务是对3D物体的各项顶点进行平移、旋转或者缩放等操作。光照计算的任务是根据程序送入的光源位置、性质、各通道强度、物体的材质等，同时再根据一定的光照数学模型计算各顶点的光照情况。
 4. 图元装配
    * 这个阶段主要有两个任务，一个是图元组装，另一个是图元处理。所谓图元组装是指顶点数据根据设置的绘制方式被结合成完整的图元。例如，点绘制方式仅需要一个单独的顶点，此方式下每个顶点为一个图元；线段绘制方式则需要两个顶点，此方式下每两个顶点构成一个图元；三角形绘制方式需要3个顶点构成一个图元。
    * 图元处理最重要的工作是剪裁，其任务是消除位于半空间(half-space)之外的部分几何图元，这个半空间是由一个剪裁平面所定义的。例如：点剪裁就是简单地接受或者拒绝顶点，线段或者多边形剪裁可能需要增加额外的顶点，具体取决于直线或者多边形与剪裁平面之间的位置关系。
    * 之所以要进行剪裁是因为随着观察位置、角度的不同，并不总能看到（这里可以简单的理解为显示到设备屏幕上）特定的3D物体某个图元的全部。
    * 剪裁时，若图元完全位于视景体以及自定义剪裁平面的内部，则将图元传递到后面的步骤进行处理；如果其完全位于视景体或者自定义剪裁平面的外部，则丢弃该图元；如果其有一部分位于内部，另一部分位于外部，则需要剪裁该图元。
 5. 光栅化
    * 虽然虚拟3D世界中的几何信息是三维的，但是由于目前用于显示的设备都是二维的。因此在真正执行光栅化工作之前，首先需要将虚拟3D世界中的物体投影到视平面上，需要注意的是，由于观察位置的不同，同一个3D场景中的物体投影到视平面可能会产生不同的效果。
    * 另外，由于虚拟3D世界当中物体的几何信息一般采用连续的数学量来表示，因此投影的平面结果也是用连续数学量来表示的。但目前的显示设备都是离散化的（由一个一个像素组成），因此还需要将投影的结果离散化。将其分解为一个一个离散化的小单元，这些小单元一般称之为片元。
    * 其实，每个片元都对应于帧缓冲对象中的一个像素，之所以不直接称之为像素是因为3D空间中的物体是可以相互遮挡的。二一个3D场景最终显示到屏幕上虽然是一个整体，但每个3D物体的每个图元是独立处理的。这就可能出现这样的情况，系统先处理的是位于离观察点较远的图元，其光删化成为了一组片元，暂时送入帧缓冲的对应位置。
    * 但后面继续处理离观察点较近的图元时也光栅化出了一组片元，两组片元中有对应到帧缓冲中同一个位置的，这时距离近的片元将覆盖距离远的片元（如何覆盖的检测是在深度检测阶段完成）。因此某片元就不一定能成为最终屏幕上显示的像素，称之为像素不准确了，可以将其理解为候选像素。
 6. 纹理环境和颜色求和
    * 该阶段主要有两个任务，具体如下所列。
      1.  一个是纹理采样任务，其主要是根据当前需要处理片元的纹理坐标及采用的纹理id对相应的纹理图进行纹理采样，获取其采样值 ` 所谓纹理采样值就是从纹理图中某个纹理坐标位置获取的一个颜色值 `
      2. 另一个是颜色求和，主要是执行颜色的变化，其根据纹理采用及光照计算等的结果综合生成需处理片元的颜色。
 7. 雾
    * 该阶段主要是根据程序中设置的雾的相关参数，如：颜色、浓度、范围等，以及某种雾的数学模型来计算当前处理的片元受雾影响后的颜色。
 8. Alpha测试
    * 如果程序中启用了Alpha测试，OpenGL Es 会检查每个片元的Alpha的值，只有Alpha的值符合测试条件的片元才会送入下一阶段，不满足条件的片元则被丢弃。
 9. 剪裁测试
    * 如果程序中启用了剪裁测试，OpenGL Es会检查每个片元在帧缓冲中对应的位置，若对应位置在剪裁窗口中则将此片元送入下一阶段，否则丢弃此片元。
 10.  深度测试和模板测试
    * 深度测试是指将输入片元的深度值与帧缓冲区存储的对应位置片元的深度值进行比较，若输入片元的深度值小则将该输入片元送入下一阶段准备覆盖帧缓冲中的原片元或与缓冲中的原片元混合，否则丢弃输入片元。
    * 模板测试的主要功能为将绘制区域限定在一定的范围内，一般用在湖面倒影、镜像等场合，后面的章节会详细介绍。
 11.  颜色缓冲混合
    * 若程序中开启了Alpha混合，则根据混合因子将上一阶段送来的片元与帧缓冲中对应的位置的片元进行Alpha混合；否则送入的片元将覆盖帧缓冲中对应位置的片元。
 12.  抖动
    * 抖动是一种简单的操作，其允许用户只使用少量的颜色模拟出更宽的颜色显示范围，从而使颜色视觉效果更加地丰富。例如，可以使用白色以及黑色模拟出一种过渡的灰色。
    * 但使用抖动也是有其固有缺点的，那就是会损失一部分分辨率，因此对于现在主流的原生颜色就很丰富的显示设备一般是不需要启用抖动的。
    * 当下的一些系统中虽然在api方面支持开启抖动，但这仅仅是为了API的兼容，其可能根本不会去进行事实上的抖动操作。
 13.  帧缓冲
    * OpenGL Es  的物体绘制并不是直接在屏幕上进行的，而是预先在帧缓冲区中进行绘制，每绘制完一帧再将绘制的结果交换到屏幕上。因此，在每次绘制新的一帧时都需要清除缓冲区中相关数据，否则有可能产生不正确的绘制效果。
    同时需要了解的是为了应对不同方面的需要，帧缓冲是由一套组件组成的，主要包括颜色缓冲、深度缓冲以及模板缓冲，各组件的具体用途如下所列：
    * 颜色缓冲用于存储每个片元的颜色值，每个颜色值包括RGBA(红，绿，蓝，透明度)4个色彩通道，应用程序运行时在屏幕上看到的就是颜色缓冲中的内容。
    * 深度缓冲用来存储每个片元的深度值，所谓深度值是指以特定的内部格式表示的从片元处到观察点（摄像机）的距离。在启用深度测试的情况下，新片元想进入帧缓冲时需要将自己的深度值与帧缓冲区中对应位置片元的深度值进行比较，若结果为小于才有可能进入缓冲，否则丢弃。
    * 模板缓冲用来存储每个片元的模板值，供模板测试使用。模板测试是几何测试中最为灵活和复杂的一种，后面将由专门的章节进行介绍。

### 1.2.2 OpenGL ES 2.0的渲染管线
 * Open GL ES 1.x 只是对开发人员开发了其中的一些API接口,但在整个渲染管线的运行过程中开发人员是不能直接干预的。因此，虽然OpenGL Es 1.x 的渲染管线功能已经很强大，但是其留给编程人员发挥的空间不大，很多特效难以开发，而OpenGL Es 2.0 则为编程人员提供了更多的发挥空间。
  * 总的来说，OpenGL ES 2.0与OpenGL ES 1.x 的渲染管线还是存在着很多的相似性，只是OpenGL ES 2.0管线中有一些部分已经改变。在这个改变的部分中，编程人员可以插入自己的一些特殊操作，方便更高效的完成OpenGL ES 1.x 难以完成的任务。
  * OpenGL ES 2.0与1.x的渲染管线还是有不少区别的，具体如下所列.
 	* OpenGl ES 2.0中的 "顶点着色器" 取代了OpenGL ES 1.x渲染管线中的"变换和光照"阶段，这使得开发3D场景时顶点的变换、法向量的计算、纹理坐标的变换、光照与材质的应用等均由开发者使用着色器代码完成，灵活性大大增强，可以轻松实现很多OpenGL ES 1.x 难以完成的低昂点处理任务。
 	* OpenGL ES 2.0中的"片元着色器"取代了OpenGL ES 1.x渲染管线中的"纹理环境和颜色求和"、"雾"以及"Alpha测试"等阶段，这使得纹理处理、颜色求和以及雾效果均由开发者自行开发，大大增强了程序对片元的处理能力，可以轻松实现很多OpenGL ES 1.x难以完成的片元处理任务。

#### 1. 顶点着色器
* 顶点着色器是一个可编程的处理单元，功能为执行顶点的变换、光照、材质的应用与计算等顶点的相关操作，其每顶点执行一次。其工作过程为首先将原始的顶点几何信息及其他属性传送到顶点着色器中，经过自己开发的顶点着色器处理后产生的纹理坐标、颜色、点位置等后继流程需要的各项顶点信息，然后将其传递给图元装配阶段.
* 顶点着色器替代了原有固定管线的顶点变换、光照计算，开发人员可以根据自己的需求自行开发顶点变换、光照等功能，大大增加程序的灵活性。但是凡事有理解有弊，增加灵活性的同时也增加了开发的难度，由于这个原因，对初学者而言可能会感觉OpenGL ES 2.0比OpenGL ES 1.x难很多，不易上手。
* 顶点着色器的输入主要为待处理顶相应的attribute（属性）变量，uniform（全局）变量、采样器以及临时变量，输出主要为经过顶点着色器后生成的varying(易变)变量以及一些内建输出变量.
* attribute变量指的是3D物体中每个顶点各自不同的信息所属的变量，一般顶点的位置、颜色、法向量等每个顶点各自不同的信息都是以attribute变量的方式传入顶点着色器的。
* uniform变量指的是对于同一组顶点组成的单个3D物体中的所有顶点都相同量，一般为场景中当前的光源位置、当前的摄像机位置、投影系列矩阵等
* varyng变量（易变变量）是从顶点着色器计算产生并传递到片元着色器的数据变量，顶点着色器可以使用易变变量来传递需要插入到片元的颜色，法向量、纹理坐标等任意值
* 内建变量gl_Position、gl_FrontFacing和gl_PointSize等。gl_Position是经过变换矩阵变换，投影后的顶点的最终位置，gl_FrontFacing指的是片元所在面的朝向，gl_PointSize指的是点的大小。
        易变变量在顶点着色器赋值后并不是直接将赋的值送入到后继的片元着色器中，而是在光栅化阶段由管线根据片元所属图元各个顶点对应的顶点着色器对此易变变量的赋值情况及片元与各项点的位置关系插值产生。
#### 2. 片元着色器
### 1.2.3 OpenGL ES 中立体物体的构建