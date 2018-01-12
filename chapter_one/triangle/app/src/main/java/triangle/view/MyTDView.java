package triangle.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import triangle.shape.Triangle;

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
