package com.kostya.scalesnetwork.transferring;

import android.content.Context;
import com.kostya.serializable.CommandObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Kostya on 13.07.2016.
 */
public class ServerThread extends Thread {
    private final Context context;
    private final Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private InterfaceInterruptThread interfaceInterrupt;

    /** Конструктор потока для соединения.
     * @param context Контекст приложения.
     * @param accept Socket для соединения.
     * @param in Обратный вызов когда поток прерван.
     */
    public ServerThread(Context context, Socket accept, InterfaceInterruptThread in) {
        this(context, accept);
        interfaceInterrupt = in;
    }
    public ServerThread(Context context, Socket accept) {
        this.context = context;
        socket = accept;
    }

    @Override
    public void run() {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            /* Вызываем для додавления в список. */
            interfaceInterrupt.onAddThread(this);
            /* Получаем данные пока нет разрыва. */
            while (!isInterrupted()){
                Object object = objectInputStream.readObject();
                if (object !=null){
                    /* Выполняем принятую команду. */
                    ((CommandObject)object).execute(context, objectOutputStream);
                }
            }
        } catch (Exception e) {
            closeSocket();
        }finally{
            closeSocket();
        }
        /* Вызывем прерывание потока. */
        interrupt();
    }

    @Override
    public void interrupt() {
        /* Передаем в интерфейс прерваный поток для удаления из списка. */
        interfaceInterrupt.onRemoveThread(this);
        super.interrupt();
    }

    private void closeSocket() {
        try {objectInputStream.close();} catch (Exception e) {}
        try {objectOutputStream.close();} catch (Exception e) {}

        try {
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (Exception e) {}
    }

    /** Отправляем данные.
     * @param o Обьект для отправки.
     * @throws IOException
     */
    void writeObject(Object o) throws IOException {
        objectOutputStream.writeObject(o);
    }
}
