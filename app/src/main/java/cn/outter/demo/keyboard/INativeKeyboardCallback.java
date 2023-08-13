package cn.outter.demo.keyboard;

public interface INativeKeyboardCallback
{
    public void OnTextEditUpdate(String text, int selectionStartPosition, int selectionEndPosition);
    public void OnAutofillUpdate(String text, int autofillType);
    public void OnKeyboardShow(int height);
    public void OnKeyboardHide(int reason);
    public void OnKeyboardDone();
    public void OnKeyboardNext();
    public void OnKeyboardCancel();
    public void OnSpecialKeyPressed(int specialKeyCode);
    public void OnKeyboardHeightChanged(int height);
    public void OnHardwareKeyboardChanged(boolean connected);
    public void OnKeyboardSendClick();
}
