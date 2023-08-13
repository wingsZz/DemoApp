package cn.outter.demo.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.outter.demo.R;

public class EditFrameLayout extends FrameLayout {
    public EditFrameLayout(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.outter_layout_edit_text,this,true);
    }

    public EditFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.outter_layout_edit_text,this,true);
    }

    public EditFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.outter_layout_edit_text,this,true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("NativeKeyboard","onTouchEvent event = " + event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("NativeKeyboard","dispatchTouchEvent -- ev = " + ev);
        return super.dispatchTouchEvent(ev);
    }
}
