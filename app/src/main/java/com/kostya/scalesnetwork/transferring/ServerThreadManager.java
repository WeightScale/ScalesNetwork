package com.kostya.scalesnetwork.transferring;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class ServerThreadManager implements InterfaceInterruptThread {
    private static final String TAG = ServerThreadManager.class.getName();
    private ServerThreadProcess serverThreadProcess;
    private ArrayList<ServerThread> listServerThreads = new ArrayList<ServerThread>();

    @Override
    public void onRemoveThread(ServerThread thread) {
        //while (!thread.isInterrupted());
        listServerThreads.remove(thread);
    }

    @Override
    public void onAddThread(ServerThread thread) {
        /** Добавляем в список. */
        listServerThreads.add(thread);
    }

    /** запускаем поток для обработки подключений.
     * @param context Контекст приложения.
     */
    public void startServerProcessorThread(Context context) {
        stopServerProcessorThread();
        try {
            serverThreadProcess = new ServerThreadProcess(context, this);
            serverThreadProcess.start();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void stopServerProcessorThread() {

        try {
            if (serverThreadProcess!=null)
                serverThreadProcess.closedSocket();

            if (serverThreadProcess != null)
                serverThreadProcess.interrupt();

        } catch (IOException e) {
            //// TODO: 09.07.2016  
        }
    }

    /** Получить список потоков которые имеют соединения.
     * @return Список
     */
    public ArrayList<ServerThread> getListServerThreads() {return listServerThreads;}

}
