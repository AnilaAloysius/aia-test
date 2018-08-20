package aia.test.android.com.aiatest.interfaces;

public interface IImgurSenderObserver {

    void onNotifySuccessfulImgurSending(String jsonResponse);
    void onNotifyFailedImgurSending(String jsonResponse);


}
