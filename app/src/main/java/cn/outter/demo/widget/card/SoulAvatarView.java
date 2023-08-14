//package cn.outter.demo.widget.card;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffXfermode;
//import android.graphics.RectF;
//import android.graphics.drawable.Animatable;
//import android.graphics.drawable.Drawable;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import androidx.annotation.ColorInt;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.content.ContextCompat;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
//import com.bumptech.glide.load.resource.gif.GifDrawable;
//import com.bumptech.glide.request.target.CustomTarget;
//import com.bumptech.glide.request.transition.Transition;
//
//import cn.mate.android.utils.MateActivityUtil;
//import cn.mate.android.utils.MateScreenUtil;
//import cn.soul.android.widget.image.CDNHelper;
//import cn.soul.android.widget.image.MateRequestOptions;
//import cn.soul.insight.log.core.SLogKt;
//import cn.soulapp.android.lib.common.utils.cdn.CDNSwitchUtils;
//import cn.soulapp.android.lib.common.utils.env.ApiEnv;
//import cn.soulapp.android.lib.common.view.RoundImageView;
//import cn.soulapp.lib.basic.utils.Dp2pxUtils;
//
//
///**
// * Soul 头像控件的封装，目前还没有实现生日帽
// * </p>
// * 在ListView或者RecyclerView中使用时，务必调用{@link SoulAvatarView#clearState()}方法，清空控件内部绘制状态，防止绘制错乱
// * <p>
// * 由于挂件的存在，有一个头像大小改变的算式。让设计直接给出挂件的大小，根据 width/10 计算出头像padding。
// * <p>
// * 统一使用 {@link cn.soulapp.android.square.utils.HeadHelper) 加载，建议使用HeadHelper#setNewAvatar
// */
//public class SoulAvatarView extends RoundImageView {
//    private RectF mRectF;
//    private float mLabelWidth;
//    private float mLabelHeight;
//    private int mLabelBackgroundColor;
//    private int mLabelRadius;
//    private int mLabelMarginBottom;
//    private int mLabelTextColor;
//    private float mLabelTextSize;
//    //todo:或许可以直接用颜色替代老头像下载的背景图片
//    private int mAvatarBackgroundColor;
//    private Drawable mAvatarBackgroundDrawable;
//    private Drawable mGuardianPendantDrawable;
//    private Drawable mOnlineStatusDrawable;
//    private String mLabelText;
//    protected Paint mPaint;
//
//    private boolean mShowLabel;
//    private boolean mShowOnlineStatus;
//    private boolean mShowPendant;
//
//    /**
//     * 是否使用挂件默认计算的padding
//     */
//    private boolean pendantPaddingEnable = true;
//    private int mPendantPadding = 0;
//    private int mHasPendantPadding = 0;
//    private int mNoPendantPadding = 0;
//
//    private int mRealPaddingLeft;
//    private int mRealPaddingRight;
//    private int mRealPaddingTop;
//    private int mRealPaddingBottom;
//
//    private int mOnlineStatusRadius;
//    private int mOnlineStatusBorderWidth;
//    private int mOnlineStatusBorderColor;
//
//
//    //数字藏品8边形（逻辑隔离）
//    private Paint mOctagonPaint;
//    private PorterDuffXfermode mMaskXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
//    private PorterDuffXfermode mStrokeXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OVER);
//    private Bitmap mOctagonMaskBitmap;//8边形蒙层
//    private Bitmap mOctagonStorkeBitmap;//8边形蒙层描边
//    private Bitmap mOctagonPendantBitmap;//8边形蒙层挂件
//    private String mAvatarName = "";//头像名称
//    private int resizeW = 0;
//    private int resizeH = 0;
//    private int mOctagonPendantBitmapWidth;
//
//    private int dp24 = MateScreenUtil.INSTANCE.dp2px(24f);
//    private int dp32 = MateScreenUtil.INSTANCE.dp2px(32f);
//
//    private boolean mShowOctagonPendantBitmap = true;//是否展示8边型专属挂件
//
//    private static double sRadians = Math.cos(Math.toRadians(45));
//
//    private Drawable.Callback mCallback = new Drawable.Callback() {
//        @Override
//        public void invalidateDrawable(@NonNull Drawable who) {
//            if (mGuardianPendantDrawable instanceof GifDrawable ||
//                    mGuardianPendantDrawable instanceof WebpDrawable) {
//                mGuardianPendantDrawable = who;
//                invalidate();
//            }
//        }
//
//        @Override
//        public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
//            SoulAvatarView.this.scheduleDrawable(who, what, when);
//        }
//
//        @Override
//        public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
//            SoulAvatarView.this.unscheduleDrawable(who, what);
//        }
//    };
//
//    public SoulAvatarView(Context context) {
//        this(context, null);
//    }
//
//    public SoulAvatarView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public SoulAvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SoulAvatarView);
//        mShowLabel = ta.getBoolean(R.styleable.SoulAvatarView_show_label, false);
//        mLabelHeight = ta.getDimension(R.styleable.SoulAvatarView_label_height, 0);
//        mLabelWidth = ta.getDimension(R.styleable.SoulAvatarView_label_width, 0);
//        mLabelBackgroundColor = ta.getColor(R.styleable.SoulAvatarView_label_background, Color.WHITE);
//        mLabelRadius = (int) ta.getDimension(R.styleable.SoulAvatarView_label_radius, 0);
//        mLabelMarginBottom = (int) ta.getDimension(R.styleable.SoulAvatarView_label_marginBottom, 0);
//        mLabelTextColor = ta.getColor(R.styleable.SoulAvatarView_label_textColor, Color.WHITE);
//        mLabelTextSize = ta.getDimension(R.styleable.SoulAvatarView_label_textSize, 6);
//        mLabelText = ta.getString(R.styleable.SoulAvatarView_label_text);
//        mAvatarBackgroundDrawable = ta.getDrawable(R.styleable.SoulAvatarView_avatar_background_drawable);
//        mShowOnlineStatus = ta.getBoolean(R.styleable.SoulAvatarView_show_online_status, false);
//        mShowPendant = ta.getBoolean(R.styleable.SoulAvatarView_show_pendant, false);
//        //mOnlineStatusDrawable = ta.getDrawable(R.styleable.SoulAvatarView_online_status_color);
//        mOnlineStatusDrawable = getContext().getDrawable(R.drawable.bg_gar_green);
//        mOnlineStatusRadius = (int) ta.getDimension(R.styleable.SoulAvatarView_online_status_radius, dpToPx(5));
//        mOnlineStatusBorderWidth = (int) ta.getDimension(R.styleable.SoulAvatarView_online_status_border_width, dpToPx(1));
//        mOnlineStatusBorderColor = ta.getColor(R.styleable.SoulAvatarView_online_status_border_color, Color.WHITE);
//        ta.recycle();
//        mRectF = new RectF();
//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        mPaint.setTextAlign(Paint.Align.CENTER);
//        setIsCircle(true);
//        mRealPaddingLeft = getPaddingLeft();
//        mRealPaddingRight = getPaddingRight();
//        mRealPaddingBottom = getPaddingBottom();
//        mRealPaddingTop = getPaddingTop();
//        // 8边形相关
//        mOctagonPaint = new Paint();
//        mOctagonPaint.setAntiAlias(true);
//        setContentDescription("头像");
//    }
//
//    public void setShowOctagonPendantBitmap(boolean showOctagonPendantBitmap) {
//        mShowOctagonPendantBitmap = showOctagonPendantBitmap;
//    }
//
//    /**
//     * 展示nft下标，必须要设置avatarName
//     * @param avatarName
//     */
//    public void setAvatarNameOrUrl(String avatarName) {
//        if (avatarName == null) {
//            return;
//        }
//        if (avatarName.startsWith("http")) {
//            try {
//                mAvatarName = avatarName.split("heads/")[1].split("\\?")[0].split("\\.")[0];
//                loadOctagonPendantBitmap();
//            } catch (Exception e) {
//                SLogKt.SLogApi.e("SoulAvatarView", "#setAvatarNameOrUrl url is not nft : " + avatarName);
//            }
//        } else {
//            mAvatarName = avatarName;
//            loadOctagonPendantBitmap();
//        }
//    }
//
//    public String getAvatarName() {
//        return mAvatarName;
//    }
//
//    public void setAvatarBackgroundDrawable(Drawable drawable) {
//        mAvatarBackgroundDrawable = drawable;
//        invalidate();
//    }
//
//
//    public Drawable getAvatarBackgroundDrawable() {
//        return mAvatarBackgroundDrawable;
//    }
//
//    public void setLabelSize(float width, float height) {
//        mLabelWidth = width;
//        mLabelHeight = height;
//        invalidate();
//    }
//
//    public void setLabelText(String text) {
//        mLabelText = text;
//        invalidate();
//    }
//
//    public void setLabelBackgroundColor(@ColorInt int color) {
//        mLabelBackgroundColor = color;
//        invalidate();
//    }
//
//    public void setLabelCornerRadius(int radius) {
//        mLabelRadius = radius;
//        invalidate();
//    }
//
//    public void setLabelTextColor(int labelTextColor) {
//        mLabelTextColor = labelTextColor;
//        invalidate();
//    }
//
//    public void setLabelTextSize(float labelTextSize) {
//        mLabelTextSize = labelTextSize;
//        invalidate();
//    }
//
//    public int getLabelMarginBottom() {
//        return mLabelMarginBottom;
//    }
//
//    public void setLabelMarginBottom(int labelMarginBottom) {
//        mLabelMarginBottom = labelMarginBottom;
//        invalidate();
//    }
//
//    public int getLabelTextColor() {
//        return mLabelTextColor;
//    }
//
//    public float getLabelTextSize() {
//        return mLabelTextSize;
//    }
//
//    public String getLabelText() {
//        return mLabelText;
//    }
//
//    public void setLabelWithLoveBell() {
//        setLabelText(getContext().getResources().getString(R.string.avatar_love_bell));
//        setLabelBackgroundColor(0xFFED6DBF);
//    }
//
//    /**
//     * (方法展示有问题，不可用，先标记过时，以后不可用)
//     * 括号为历史老注释，目前没发现问题，继续使用
//     * @param isShown
//     */
//    public void setShowLabel(boolean isShown) {
//        mShowLabel = isShown;
//        invalidate();
//    }
//
//    public void setLabelWithPlanet() {
//        setLabelText(getContext().getResources().getString(R.string.avatar_planet));
//        setLabelBackgroundColor(0xFF84D5BF);
//    }
//
//    public void setLabelWithVideoMatch() {
//        setLabelText(getContext().getResources().getString(R.string.avatar_video_match));
//        setLabelBackgroundColor(0xFFA021EF);
//    }
//
//    public void setLabelWithFriend() {
//        setLabelText(getContext().getResources().getString(R.string.avatar_friend));
//        setLabelBackgroundColor(0xFF6BE5ED);
//    }
//
//    public void setLabelWithMatch() {
//        setLabelText(getContext().getResources().getString(R.string.avatar_match));
//        setLabelBackgroundColor(0xFFF5E667);
//    }
//
//    public void setLabelWithGroup() {
//        setLabelText(getContext().getResources().getString(R.string.avatar_group));
//        setLabelBackgroundColor(0xFF6BE5ED);
//    }
//
//    public void setLabelInWolf() {
//        setLabelText(getContext().getResources().getString(R.string.avatar_in_wolf));
//        setLabelBackgroundColor(0xFF6BE5ED);
//    }
//
//    public void setShowOnlineStatus(boolean show) {
//        mShowOnlineStatus = show;
//        invalidate();
//    }
//
//    public void setShowPendant(boolean show) {
//        mShowPendant = show;
//        setPendantPaddingInternal();
//        invalidate();
//    }
//
//    public boolean isShowPendant() {
//        return mShowPendant;
//    }
//
//    private void setPendantPaddingInternal() {
//        if (mShowPendant) {
//            mNoPendantPadding = (int) ((1 - 3f / 4f) * getWidth() / 2f);
//            mHasPendantPadding = (int) ((1 - 3f / 4f) * getWidth() / 2f);
//            mPendantPadding = mNoPendantPadding;
//            refreshPadding();
//        }
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        // todo 在此处调用此方法初始化 mNoPendantPadding 和 mHasPendantPadding 不合理，后续修改
//        setPendantPaddingInternal();
//        super.onSizeChanged(w, h, oldw, oldh);
//        resizeW = w;
//        resizeH = h;
//        if (isOctagon()) {
//            resizeOctagon(w, h);
//        }
//    }
//
//    @Override
//    public void setOctagonStyle(boolean isOctagon) {
//        super.setOctagonStyle(isOctagon);
//        if (isOctagon) {
//            resizeOctagon(resizeW, resizeH);
//        }
//        invalidate();
//    }
//
//    @Override
//    public void setBorderWidth(int borderWidth) {
//        super.setBorderWidth(borderWidth);
//        if (isOctagon()) {
//            resizeOctagon(getWidth(), getHeight());
//            invalidate();
//        }
//    }
//
//    /**
//     * 是否使用挂件默认计算的padding
//     */
//    public void setPendantPaddingEnable(boolean enable) {
//        this.pendantPaddingEnable = enable;
//    }
//
//    @Override
//    public void setPadding(int left, int top, int right, int bottom) {
//        mRealPaddingLeft = left;
//        mRealPaddingTop = top;
//        mRealPaddingRight = right;
//        mRealPaddingBottom = bottom;
//        if (pendantPaddingEnable) {
//            super.setPadding(left + mPendantPadding,
//                    top + mPendantPadding,
//                    right + mPendantPadding,
//                    bottom + mPendantPadding);
//        } else {
//            super.setPadding(left,
//                    top,
//                    right,
//                    bottom);
//        }
//
//
//    }
//
//    public void setGuardianPendant(Drawable drawable) {
//        // 目前存在的两种动态drawable(GifDrawable，APNGDrawable)都继承 Animatable
//        if (mGuardianPendantDrawable instanceof Animatable) {
//            ((Animatable) mGuardianPendantDrawable).stop();
//            mGuardianPendantDrawable.setCallback(null);
//            mGuardianPendantDrawable.setVisible(false, false);
//        }
//        mGuardianPendantDrawable = drawable;
//        mShowPendant = true;
//        if (drawable instanceof GifDrawable) {
//            GifDrawable gifDrawable = (GifDrawable) drawable;
//            gifDrawable.setCallback(mCallback);
//            gifDrawable.setLoopCount(GifDrawable.LOOP_FOREVER);
//            gifDrawable.start();
//            mPendantPadding = mNoPendantPadding;
//        } else if (drawable instanceof WebpDrawable) {
//            WebpDrawable webpDrawable = (WebpDrawable) drawable;
//            webpDrawable.setCallback(mCallback);
//            webpDrawable.start();
//            mPendantPadding = mNoPendantPadding;
//        } else {
//            mPendantPadding = mHasPendantPadding;
//        }
//        refreshPadding();
//        invalidate();
//    }
//
//    public Drawable getGuardianPendantDrawable() {
//        return mGuardianPendantDrawable;
//    }
//
//    public void resumeAnim() {
//        if (mGuardianPendantDrawable instanceof Animatable) {
//            ((Animatable) mGuardianPendantDrawable).start();
//        }
//    }
//
//    public void stopAnim() {
//        if (mGuardianPendantDrawable instanceof Animatable) {
//            ((Animatable) mGuardianPendantDrawable).stop();
//        }
//    }
//
//    private void refreshPadding() {
//        setPadding(mRealPaddingLeft, mRealPaddingTop, mRealPaddingRight, mRealPaddingBottom);
//    }
//
//    @Override
//    protected void onWindowVisibilityChanged(int visibility) {
//        super.onWindowVisibilityChanged(visibility);
//        if (visibility == VISIBLE) {
//            resumeAnim();
//        } else {
//            stopAnim();
//        }
//    }
//
//    /**
//     * 在列表View中使用时，务必在加载前调用此方法
//     */
//    public void clearState() {
//        mAvatarBackgroundDrawable = null;
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        if (isOctagon()) {
//            drawOctagon(canvas);
//        }
//        //计算label区域并且绘制
//        int width = getWidth();
//        int height = getHeight();
//
//        int paddingLeft = getPaddingLeft();
//        int paddingTop = getPaddingTop();
//        int paddingRight = getPaddingRight();
//        int paddingBottom = getPaddingBottom();
//        if (mAvatarBackgroundDrawable != null) {
//            mAvatarBackgroundDrawable.setBounds(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
//            mAvatarBackgroundDrawable.draw(canvas);
//        }
//        if (!isOctagon()) {
//            super.onDraw(canvas);
//        }
//        // 绘制挂件
//        drawaPendant(canvas, width, height);
//        //画8边型专属标签
//        if (isOctagon() && !mShowLabel) {
//            if (mOctagonPendantBitmap != null && mShowOctagonPendantBitmap && !mOctagonPendantBitmap.isRecycled()) {
//                canvas.drawBitmap(mOctagonPendantBitmap, (getWidth() - mOctagonPendantBitmap.getWidth()) / 2f,
//                        getHeight() - getPaddingBottom() - mOctagonPendantBitmap.getHeight() / 2f, mOctagonPaint);
//            }
//        }
//        //drawOnlineStatus
//        drawOnLineStatus(canvas);
//
//        if (mShowLabel && width >= dp32) {
//            float labelTop = height - mLabelHeight - mLabelMarginBottom;
//            float labelLeft = (width - mLabelWidth) / 2;
//            mRectF.set(labelLeft, labelTop, labelLeft + mLabelWidth, labelTop + mLabelHeight);
//            mPaint.setColor(mLabelBackgroundColor);
//            canvas.drawRoundRect(mRectF, mLabelRadius, mLabelRadius, mPaint);
//
//            //绘制文字
//            if (!TextUtils.isEmpty(mLabelText)) {
//                mPaint.setColor(mLabelTextColor);
//                mPaint.setTextSize(mLabelTextSize);
//                float top = mPaint.getFontMetrics().top;
//                float bottom = mPaint.getFontMetrics().bottom;
//                int baseLineY = (int) (labelTop + mLabelHeight / 2 - top / 2 - bottom / 2);
//
//                canvas.drawText(mLabelText, labelLeft + mLabelWidth / 2, baseLineY, mPaint);
//            }
//        }
//    }
//
//    private void drawaPendant(Canvas canvas, int width, int height) {
//        if (mShowPendant && mGuardianPendantDrawable != null) {
//            mGuardianPendantDrawable.setBounds(0, 0, width, height);
//            mGuardianPendantDrawable.draw(canvas);
//        }
//    }
//
//    private void drawOnLineStatus(Canvas canvas) {
//        if (mShowOnlineStatus && !mShowLabel) {
//            float offset = (float) ((mRadius + mBorderWidth) * sRadians);
//            //mPaint.setColor(mOnlineStatusDrawable);
//            int onLineX = (int) (mCx + offset - mOnlineStatusRadius);
//            int onLineY = (int) (mCy + offset - mOnlineStatusRadius);
//            mOnlineStatusDrawable.setBounds(onLineX, onLineY, onLineX + mOnlineStatusRadius * 2, onLineY + mOnlineStatusRadius * 2);
//            mOnlineStatusDrawable.draw(canvas);
//            mPaint.setColor(mOnlineStatusBorderColor);
//            mPaint.setStyle(Paint.Style.STROKE);
//            mPaint.setStrokeWidth(mOnlineStatusBorderWidth);
//            canvas.drawCircle(mCx + offset, mCy + offset, mOnlineStatusRadius + mOnlineStatusBorderWidth / 2f, mPaint);
//        }
//    }
//
//    private int dpToPx(int dps) {
//        return Math.round(getResources().getDisplayMetrics().density * dps);
//    }
//
//    public void setOnlineStatusBorderColor(int color) {
//        mOnlineStatusBorderColor = getContext().getResources().getColor(color);
//    }
//
//    public void setOnlineStatusBorderWidth(int borderWidth) {
//        mOnlineStatusBorderWidth = borderWidth;
//    }
//
//    public void setOnlineStatusRadius(int onlineStatusRadius) {
//        this.mOnlineStatusRadius = onlineStatusRadius;
//    }
//
//
//    /********数字藏品逻辑写这里（8边形）*********/
//
//    @SuppressLint("RestrictedApi")
//    private void resizeOctagon(int w, int h) {
//        if (w == 0 || h == 0) {
//            return;
//        }
//        int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
//        int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
//        //理想情况下蒙层的高度
//        int octagonBitmapHeight = contentHeight - mBorderWidth * 2;
//        int octagonBitmapWidth = contentWidth - mBorderWidth * 2;
////        int radius = octagonBitmapWidth / 2;
////        double radians = Math.toRadians(67.5);
////        double realRadius = Math.sin(radians) * radius;
//        //理想情况下描边的宽高
//        int octagonStrokeHeight = contentHeight;
//        int octagonStrokeWidth = contentWidth;
//        //理想情况下挂件的宽高
//        mOctagonPendantBitmapWidth = Math.min(contentWidth / 3, Dp2pxUtils.dip2px(24));
//        //宽高中最小的一遍大于28dp才展示8边型专属挂件
//        mShowOctagonPendantBitmap = Math.min(getWidth(), getHeight()) > Dp2pxUtils.dip2px(28) && mShowOctagonPendantBitmap;
//        //预先计算一下,如果蒙层底边+（1/2）的挂件高度超过了view的本身高度，那么说明需要缩放了
//        // (4f / 25) * octagonPendantBitmapHeight 切图自带的边框高度
////        float needHeight = contentHeight + getPaddingTop() + octagonPendantBitmapHeight / 2f - (4f / 25) * octagonPendantBitmapHeight;
////        if (needHeight > getHeight()) {
////            float value = needHeight - getHeight();
////            octagonBitmapHeight -= value;
////            octagonBitmapWidth -= value;
////            octagonStrokeHeight -= value;
////            octagonStrokeWidth -= value;
////            octagonPendantBitmapHeight -= value;
////            octagonPendantBitmapWidth -= value;
////
////        }
//        loadOctagonPendantBitmap();
//        mOctagonMaskBitmap = getOctagonMaskBitmap(octagonBitmapWidth, octagonBitmapHeight);
//        mOctagonStorkeBitmap = getOctagonMaskBitmap(octagonStrokeWidth, octagonStrokeHeight);
//    }
//
//
//    private void drawOctagon(Canvas canvas) {
//
//        int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
//        int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
//        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
//        float scaleX = (contentWidth - mBorderWidth * 2) / (float) contentWidth;
//        float scaleY = (contentHeight - mBorderWidth * 2) / (float) contentHeight;
//
//        //缩放，防止源图片漏出
//        canvas.save();
//        canvas.translate(mBorderWidth + (1 - scaleX) * getPaddingLeft(), mBorderWidth + (1 - scaleY) * getPaddingTop());
//        canvas.scale(scaleX, scaleY);
//        super.onDraw(canvas);
//        canvas.restore();
//
//        if (getDrawable() == null || mOctagonMaskBitmap == null) {
//            return;
//        }
//
//        mOctagonPaint.setXfermode(mMaskXfermode);
//        //内容区域
//        canvas.drawBitmap(mOctagonMaskBitmap, getPaddingLeft() + mBorderWidth, getPaddingTop() + mBorderWidth, mOctagonPaint);
//        mOctagonPaint.setXfermode(null);
//
//        //画描边
//        if (mBorderWidth != 0 && mOctagonStorkeBitmap != null) {
//            mOctagonPaint.setStyle(Paint.Style.FILL);
//            mOctagonPaint.setXfermode(mStrokeXfermode);
//            canvas.drawBitmap(mOctagonStorkeBitmap, getPaddingLeft(), getPaddingTop(), mOctagonPaint);
//            mOctagonPaint.setXfermode(null);
//        }
//        canvas.restoreToCount(layer);
//    }
//
//
//    //获得蒙层
//    public Bitmap getOctagonMaskBitmap(float w, float h) {
//        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_octagon);
//        Bitmap bitmap = Bitmap.createBitmap((int) w, (int) h, Bitmap.Config.ARGB_4444);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, (int) w, (int) h);
//        drawable.draw(canvas);
//        return bitmap;
//
//    }
//
//    //获得8边型专属挂件
//    private void loadOctagonPendantBitmap() {
//        if (mOctagonPendantBitmapWidth == 0) {
//            return;
//        }
//        String url = getOctagonPendantUrl();
//        if (TextUtils.isEmpty(url)) {
//            return;
//        }
//        if (MateActivityUtil.INSTANCE.isActivityFinished(getContext())) {
//            return;
//        }
//        Glide.with(this).asBitmap().apply(
//                new MateRequestOptions().centerCrop().override(mOctagonPendantBitmapWidth)).load(url).into(
//                new OctagonCustomTarget<Bitmap>(mAvatarName) {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource,
//                            @Nullable Transition<? super Bitmap> transition) {
//                        if (avatarName == null) {
//                            return;
//                        }
//                        if (!avatarName.equals(mAvatarName)) {
//                            return;
//                        }
//                        mOctagonPendantBitmap = resource;
//                        invalidate();
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                    }
//                });
//    }
//
//    /**
//     * 根据avatarname 固定规则拼接获取 nft 下标链接
//     * 示例 https://china-img.soulapp.cn/heads/0001-small-medal.png?x-oss-process=image/resize,m_lfit,w_120,type_2/format,webp
//     * @return
//     */
//    private String getOctagonPendantUrl() {
//        if (!isOctagon()) {
//            return null;
//        }
//        try {
//            String nftRule = ApiEnv.isTest() ? "-t-small-medal.png" : "-small-medal.png";
//            String result = mAvatarName.split("-")[1];
//            return CDNHelper.getAvatarUrl(
//                    CDNSwitchUtils.getImgDomainHttp() + "heads/" + result + nftRule,
//                    dp24);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private abstract static class OctagonCustomTarget<T> extends CustomTarget<T> {
//
//        public String avatarName = "";
//
//        public OctagonCustomTarget(String avatarName) {
//            super();
//            this.avatarName = avatarName;
//        }
//    }
//}
