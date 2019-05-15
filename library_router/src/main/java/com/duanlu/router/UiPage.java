package com.duanlu.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.AnimRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

/********************************
 * @name UiPage
 * @author 段露
 * @createDate 2019/02/12 17:27
 * @updateDate 2019/02/12 17:27
 * @version V1.0.0
 * @describe 页面跳转.
 ********************************/
public class UiPage {

    public static final String EXTRA_THEME_RES_ID = "extra_theme_res_id";//传递主题
    public static final String EXTRA_TITLE = "extra_title";//传递标题
    public static final String EXTRA_FRAGMENT_CLASS_NAME = "extra_fragment_class_name";

    private Fragment mFragmentV4;
    private Context mContext;
    private Class<?> mClazz;
    private int mRequestCode;
    private Intent mIntent;

    private static Class<? extends Activity> sShellActivityClass;

    //共享组件转场动画(5.0 API21之后)
    //退出时这样写ActivityCompat.finishAfterTransition(activity);
    //这样写防止5.0之后使用共享组件转场动画时退出动画不执行的问题.
    private Bundle mOptionsCompat;
    private int mEnterAnim = -1;//转场动画
    private int mExitAnim = -1;//转场动画

    private boolean isGreenChannel;//是否跳过拦截器:true跳过,false不跳过.

    private UiPage(Context context) {
        this.mContext = context;

        mIntent = new Intent();
    }

    private UiPage(Fragment fragmentV4) {
        this.mFragmentV4 = fragmentV4;
        this.mContext = mFragmentV4.getContext();

        mIntent = new Intent();
    }

    public static void init(Class<? extends Activity> shellActivityClass) {
        sShellActivityClass = shellActivityClass;
    }

    public static UiPage with(Context context) {
        return new UiPage(context);
    }

    public static UiPage with(Fragment fragmentV4) {
        return new UiPage(fragmentV4);
    }

    public UiPage setClass(Class<?> clazz) {
        this.mClazz = clazz;
        return this;
    }

    public UiPage addFlags(int flags) {
        mIntent.addFlags(flags);
        return this;
    }

    public UiPage setFlags(int flags) {
        mIntent.setFlags(flags);
        return this;
    }

    public UiPage addCategory(String category) {
        mIntent.addCategory(category);
        return this;
    }

    private void complete() {
        if (Fragment.class.isAssignableFrom(mClazz)) {//根据Fragment的Class对象导航到对应Fragment
            mIntent.setClass(mContext, sShellActivityClass);
            mIntent.putExtra(EXTRA_FRAGMENT_CLASS_NAME, mClazz.getName());
        } else {//导航到Activity
            mIntent.setClass(mContext, mClazz);
        }
    }

    public void navigation() {
        if (isGreenChannel) {
            _navigation();
        } else {
            UiPageInterceptorManager.getInstance().doInterceptors(mContext, this, new UiPageInterceptorCallback() {
                @Override
                public void onContinue(UiPage uiPage) {
                    _navigation();
                }

                @Override
                public void onInterrupt(int errorCode, Throwable throwable) {

                }
            });
        }
    }

    private void _navigation() {
        complete();
        if (mRequestCode != 0 && null != mFragmentV4) {
            mFragmentV4.startActivityForResult(mIntent, mRequestCode, mOptionsCompat);
        } else if (mRequestCode != 0 && mContext instanceof Activity) {
            ActivityCompat.startActivityForResult((Activity) mContext, mIntent, mRequestCode, mOptionsCompat);
        } else {
            ActivityCompat.startActivity(mContext, mIntent, mOptionsCompat);
        }
        if (mEnterAnim != -1 && mExitAnim != -1 && mContext instanceof Activity) {
            ((Activity) mContext).overridePendingTransition(mEnterAnim, mExitAnim);
        }
    }

    public UiPage openGreenChannel() {
        this.isGreenChannel = true;
        return this;
    }

    public boolean isGreenChannel() {
        return isGreenChannel;
    }

    /**
     * 携带标题
     *
     * @param title 标题
     * @return UiPage
     */
    public UiPage setTitle(String title) {
        mIntent.putExtra(EXTRA_TITLE, title);
        return this;
    }

    /**
     * 携带主题
     *
     * @param themResId 主题
     * @return UiPage
     */
    public UiPage setTheme(@StyleRes int themResId) {
        mIntent.putExtra(EXTRA_THEME_RES_ID, themResId);
        return this;
    }

    /**
     * 设置请求码
     *
     * @param requestCode 请求码
     * @return UiPage
     */
    public UiPage setRequestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    @RequiresApi(16)
    public UiPage withOptionsCompat(@Nullable ActivityOptionsCompat compat) {
        if (null != compat) {
            this.mOptionsCompat = compat.toBundle();
        }
        return this;
    }

    public UiPage withTransition(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        this.mEnterAnim = enterAnim;
        this.mExitAnim = exitAnim;
        return this;
    }

    //////////////////////////////////////////////携带参数 start//////////////////////////////////////////////
    public UiPage put(@Nullable String key, String value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, int value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, long value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, double value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, float value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, @Nullable Bundle value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, boolean value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, byte value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, char value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, @Nullable CharSequence value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, short value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, @Nullable Parcelable value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, @Nullable Serializable value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, @Nullable Parcelable[] value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, @Nullable byte[] value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, @Nullable short[] value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, @Nullable char[] value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, @Nullable float[] value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage put(@Nullable String key, @Nullable CharSequence[] value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public UiPage putIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        mIntent.putIntegerArrayListExtra(key, value);
        return this;
    }

    public UiPage putParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
        mIntent.putParcelableArrayListExtra(key, value);
        return this;
    }

    public UiPage putStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        mIntent.putStringArrayListExtra(key, value);
        return this;
    }

    public UiPage putCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        mIntent.putCharSequenceArrayListExtra(key, value);
        return this;
    }
    //////////////////////////////////////////////携带参数 end//////////////////////////////////////////////

}
