package com.alenbeyond.socketiodemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SocketIO";
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        if (socket != null && socket.connected()) {
            socket.disconnect();
        } else {
            try {
                socket = IO.socket("http://192.168.1.96:9999");
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... objects) {
                        Log.d(TAG, "连接成功");
                    }
                }).on("test", new Emitter.Listener() {
                    @Override
                    public void call(Object... objects) {
                        Log.d(TAG, "收到消息===>" + objects[0].toString());
                    }
                }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
                    @Override
                    public void call(Object... objects) {
                        Log.d(TAG, "连接超时");
                    }
                }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                    @Override
                    public void call(Object... objects) {
                        Log.d(TAG, "连接错误");
                    }
                }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... objects) {
                        Log.d(TAG, "断开连接");
                    }
                });
                socket.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

    }
}
