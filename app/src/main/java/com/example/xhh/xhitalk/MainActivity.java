package com.example.xhh.xhitalk;
import android.widget.TextView;
import com.example.common.app.Activity;
import butterknife.BindView;
public class MainActivity extends Activity {
     @BindView(R.id.txt_test_id)
     TextView mTextTest;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWdget() {
        super.initWdget();
        mTextTest.setText("hello world");

    }
}
