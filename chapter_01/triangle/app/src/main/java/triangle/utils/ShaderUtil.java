package triangle.utils;

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
