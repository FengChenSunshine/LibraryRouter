package com.duanlu.router;

import android.content.Context;

/********************************
 * @name UiPageInterceptor
 * @author 段露
 * @createDate 2019/02/12 17:29
 * @updateDate 2019/02/12 17:29
 * @version V1.0.0
 * @describe UiPage拦截器.
 ********************************/
public interface UiPageInterceptor {
    /**
     * 执行拦截操作.
     * 注：1.0版本目前仅支持同步操作.
     *
     * @param context  Context
     * @param uiPage   UiPage
     * @param callback UiPageInterceptorCallback
     */
    void process(Context context, UiPage uiPage, UiPageInterceptorCallback callback);
}
