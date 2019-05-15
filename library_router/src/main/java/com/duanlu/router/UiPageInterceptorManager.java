package com.duanlu.router;

import android.content.Context;

import java.util.Map;
import java.util.TreeMap;

/********************************
 * @name UiPageInterceptorManager
 * @author 段露
 * @createDate 2019/02/12 17:28
 * @updateDate 2019/02/12 17:28
 * @version V1.0.0
 * @describe 拦截器管理类.
 ********************************/
public class UiPageInterceptorManager {

    private static UiPageInterceptorManager sManager;
    private Map<Integer, UiPageInterceptor> mUiPageInterceptorMap;
    private UiPage mPendingUiPage;
    private int mCounter;

    private UiPageInterceptorManager() {
        this.mUiPageInterceptorMap = new TreeMap<>();
    }

    public static UiPageInterceptorManager getInstance() {
        if (null == sManager) {
            synchronized (UiPageInterceptorManager.class) {
                if (null == sManager) {
                    sManager = new UiPageInterceptorManager();
                }
            }
        }
        return sManager;
    }

    public void addUiPageInterceptor(int priority, UiPageInterceptor uiPageInterceptor) {
        mUiPageInterceptorMap.put(priority, uiPageInterceptor);
    }

    public void removeUiPageInterceptor(int priority, UiPageInterceptor uiPageInterceptor) {
        if (!mUiPageInterceptorMap.containsValue(uiPageInterceptor)) return;
        mUiPageInterceptorMap.remove(priority);
    }

    public UiPage getPendingUiPage() {
        return this.mPendingUiPage;
    }

    public void setPendingUiPage(UiPage pendingUiPage) {
        this.mPendingUiPage = pendingUiPage;
    }

    void doInterceptors(Context context, UiPage uiPage, final UiPageInterceptorCallback callback) {
        this.mCounter = mUiPageInterceptorMap.size();
        if (0 == mCounter) {
            callback.onContinue(uiPage);
        } else {
            UiPageInterceptor interceptor;
            for (Integer key : mUiPageInterceptorMap.keySet()) {
                interceptor = mUiPageInterceptorMap.get(key);
                interceptor.process(context, uiPage, new UiPageInterceptorCallback() {
                    @Override
                    public void onContinue(UiPage uiPage) {
                        mCounter--;
                        if (mCounter <= 0) callback.onContinue(uiPage);
                    }

                    @Override
                    public void onInterrupt(int errorCode, Throwable throwable) {
                        callback.onInterrupt(errorCode, throwable);
                        return;
                    }
                });
            }
        }
    }

}
