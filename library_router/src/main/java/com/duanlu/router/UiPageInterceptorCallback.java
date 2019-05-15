package com.duanlu.router;

/********************************
 * @name UiPageInterceptorCallback
 * @author 段露
 * @createDate 2019/02/12 17:29
 * @updateDate 2019/02/12 17:29
 * @version V1.0.0
 * @describe UiPage拦截器回调.
 ********************************/
public interface UiPageInterceptorCallback {

    void onContinue(UiPage uiPage);

    void onInterrupt(int errorCode, Throwable throwable);
}
