package triangle;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import triangle.view.MyTDView;


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
