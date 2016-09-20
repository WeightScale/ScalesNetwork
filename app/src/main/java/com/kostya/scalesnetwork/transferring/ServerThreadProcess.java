package com.kostya.scalesnetwork.transferring;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * @author Kostya  on 13.07.2016.
 */
public class ServerThreadProcess extends Thread {
    public static final int SERVER_PORT = 8700;
    private final Context context;
    private final ServerSocket serverSocket;
    private InterfaceInterruptThread interfaceInterruptThread;

    private static final String TAG = ServerThreadProcess.class.getName();

    /** Конструктор Просессов соединения.
     * @param context Контекст приложения.
     * @throws IOException
     */
    public ServerThreadProcess(Context context, InterfaceInterruptThread interfaceInterruptThread) throws IOException {
        this.context = context;
        this.interfaceInterruptThread = interfaceInterruptThread;
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(SERVER_PORT));
    }

    @Override
    public void run() {
        /** Выполняем пока не прерьвемся. */
        while (!isInterrupted()) {
            try {
                /** Новый поток ждет создания когда присоеденится клиент.  */
                ServerThread thread = new ServerThread(context, serverSocket.accept(), interfaceInterruptThread);
                thread.start();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /** Закрываем ServerSocket.
     * @throws IOException
     */
    public void closedSocket() throws IOException {
        if (serverSocket != null)
            serverSocket.close();
    }

}
