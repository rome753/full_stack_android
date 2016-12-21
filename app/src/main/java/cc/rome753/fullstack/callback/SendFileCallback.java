package cc.rome753.fullstack.callback;

/**
 * websocket发送文件的回调
 * Created by crc on 16/12/21.
 */
public interface SendFileCallback {
    void onSuccess();
    void onFailure(String reason);
}
