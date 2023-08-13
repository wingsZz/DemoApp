package cn.outter.demo.keyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import cn.outter.demo.R;

public class NativeKeyboard extends Fragment implements TextWatcher, TextView.OnEditorActionListener, KeyboardHeightObserver {
    /**
     * The tag of this Fragment
     */
    public static final String TAG = "NativeKeyboardFragment";

    //endregion

    /**
     * The globally accessible instance of this class
     */
    public static NativeKeyboard instance;
    public INativeKeyboardCallback unityCallback;
    public boolean updatesEnabled;
    public boolean cancelUpdateWhenDone;

    public EditFrameLayout editFrameLayout;
    public EditText currentView;
    public ImageView switchButton;
    public View mockPanel;
    public ImageView expandButton;
    public ImageView shrinkButton;
    public ImageView expandSendButton;
    public ImageView shrinkSendButton;
    public View expandView;
    public View shrinkView;
    public boolean emojisAllowed;
    public boolean hasNext;
    public boolean isPanelShowFront;


    public Handler handler;
    public Runnable updateRunnable;
    public Runnable hardwareKeyboardUpdateRunnable;
    public InputMethodManager inputMethodManager;
    public boolean keyboardVisible;
    public boolean ignoreTextChange;
    public boolean hardwareKeyboardConnected;
    public long visibleStartTime;
    public int bottomOffset;

    private String lastText;
    private int lastSelectionStartPosition;
    private int lastSelectionEndPosition;
    public int lastKeyboardHeight;
    private String longestNumberSequence;

    private long pendingStartTime;
    private boolean navigationBarWasVisible;
    private boolean initialized;

    private KeyboardHeightProvider heightProvider;
    private int systemKeyboardHeight;
    private int currentKeyboardHeight;


    public static void initialize(INativeKeyboardCallback unityCallback, AppCompatActivity activity) {
        instance = new NativeKeyboard();
        instance.unityCallback = unityCallback;
        activity.getSupportFragmentManager().beginTransaction().add(instance, TAG).commit();
    }

    public static void initialize(INativeKeyboardCallback unityCallback, Fragment fragment) {
        instance = new NativeKeyboard();
        instance.unityCallback = unityCallback;
        fragment.getChildFragmentManager().beginTransaction().add(instance, TAG).commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (instance == null) //You shouldn't be here if the instance is NULL
        {
            return;
        }

        setRetainInstance(true); // Retain between configuration changes (like device rotation)
        heightProvider = new KeyboardHeightProvider(getActivity());
        editFrameLayout = new EditFrameLayout(getActivity()) {
            @Override
            public boolean dispatchTouchEvent(MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//                    View v = getActivity().getCurrentFocus();
                    if (isShouldHideKeyboard(ev)) {
                        inputMethodManager.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
                    }
                }
                return super.dispatchTouchEvent(ev);
            }

            private boolean isShouldHideKeyboard(MotionEvent event) {
                ViewGroup viewGroup = (ViewGroup) getChildAt(0);
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View v = viewGroup.getChildAt(i);
                    int[] l = {0, 0};
                    v.getLocationInWindow(l);
                    int left = l[0],
                            top = l[1],
                            bottom = top + v.getHeight(),
                            right = left + v.getWidth();
                    if (event.getX() > left && event.getX() < right
                            && event.getY() > top && event.getY() < bottom && v.getVisibility() == View.VISIBLE) {
                        // 点击EditText的事件，忽略它。
                        return false;
                    }
                }

                // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
                return keyboardVisible;
            }
        };

        currentView = editFrameLayout.findViewById(R.id.send_edt);

        switchButton = editFrameLayout.findViewById(R.id.switchButton);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPanelShowFront = !isPanelShowFront;
                if (!isPanelShowFront) {
                    inputMethodManager.showSoftInputFromInputMethod(currentView.getWindowToken(),InputMethodManager.SHOW_FORCED);
                } else {
                    inputMethodManager.hideSoftInputFromWindow(currentView.getWindowToken(),0);
                }
            }
        });


        Activity activity = getActivity();
        inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        initialized = true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (instance == null) //You shouldn't be here if the instance is NULL
        {
            getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
//        new Handler().postDelayed(() -> {
        heightProvider.start();
//        }, 2000);
        heightProvider.setKeyboardHeightObserver(this);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        if (inputMethodManager != null && currentView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
            hideNavigationBar();
        }
        heightProvider.setKeyboardHeightObserver(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (inputMethodManager != null && currentView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
            unityCallback.OnKeyboardCancel();
            hideNavigationBar();
        }
        heightProvider.setKeyboardHeightObserver(null);
        heightProvider.close();
    }

    /********************NavigationBar & StatusBar*********************/

    private void initNavigationBar() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                //Log.d("NativeKeyboard", "VisibilityChanged: " + i);
            }
        });
    }

    public boolean isStatusBarVisible() {
        Rect rectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        return statusBarHeight != 0;
    }

    private void showNavigationBar() {
        Window window = getActivity().getWindow();
        boolean fullScreen = (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
        boolean forceNotFullScreen = (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN) != 0;

        if (fullScreen && !forceNotFullScreen && !navigationBarWasVisible) {
            View decorView = window.getDecorView();
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(flags);
        }
    }

    private boolean isLandscape() {
        return (getOrientation() == Configuration.ORIENTATION_LANDSCAPE);
    }

    private int getOrientation() {
        Activity activity = getActivity();
//        if(activity == null){activity = UnityPlayer.currentActivity;}

        try {
            return activity.getResources().getConfiguration().orientation;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    private void hideNavigationBar() {
        Window window = getActivity().getWindow();
        boolean fullScreen = (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
        boolean forceNotFullScreen = (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN) != 0;

        if (fullScreen && !forceNotFullScreen && !navigationBarWasVisible) {
            View decorView = window.getDecorView();
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(flags);
        }
    }

    private void showPanel() {
        int panelHeight = systemKeyboardHeight == 0 ? 600 : systemKeyboardHeight;
        ViewGroup.LayoutParams params = mockPanel.getLayoutParams();
        params.height = panelHeight;
        mockPanel.requestLayout();
    }

    private void hidePanel() {
        int panelHeight = 0;
        ViewGroup.LayoutParams params = mockPanel.getLayoutParams();
        params.height = panelHeight;
        mockPanel.requestLayout();
    }

    /********************NavigationBar & StatusBar*********************/

    private int getKeyboardHeight() {
        return currentKeyboardHeight;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (ignoreTextChange) {
            return;
        }

        String text = currentView.getText().toString();
        int selectionStartPosition = currentView.getSelectionStart();
        int selectionEndPosition = currentView.getSelectionEnd();
        if (selectionStartPosition > selectionEndPosition) //Check if they are swapped
        {
            selectionStartPosition = currentView.getSelectionEnd();
            selectionEndPosition = currentView.getSelectionStart();
        }

        int lineCount = currentView.getLineCount();
        expandButton.setVisibility(lineCount > 3 ? View.VISIBLE : View.GONE);
        shrinkSendButton.setVisibility(text.length() > 0 ? View.VISIBLE : View.GONE);
        unityCallback.OnTextEditUpdate(text, selectionStartPosition, selectionEndPosition);

        lastText = text;
        lastSelectionStartPosition = selectionStartPosition;
        lastSelectionEndPosition = selectionEndPosition;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (hasNext) {
                unityCallback.OnKeyboardNext();
                return true;
            } else {
                unityCallback.OnKeyboardDone();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {
        Log.d(TAG, "onKeyboardHeightChanged ---> height = " + height + ",orientation = " + orientation);
        if (height > 0) {
            // 拿到真正的系统键盘高度，并且键盘展开了
            systemKeyboardHeight = height;
            isPanelShowFront = false;
        }
        //获取当前的高度
        currentKeyboardHeight = height;

        // 判断键盘已经展开过了 && 现在收起了 && 功能面板没有展开
        if (systemKeyboardHeight > 0 && height <= 0 && !isPanelShowFront) {
            // 高度低于0时，认为键盘收起了
            Log.d(TAG, "onKeyboardHeightChanged ---> call hideKeyboard");
            hidePanel();
        }
    }
}
