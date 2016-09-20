package com.kostya.scalesnetwork.transferring;

/**
 * @author Kostya on 14.07.2016.
 */

/**
 * Для обратного вызова чтобы удалить поток из листа потоков.
 * Вызывается когда поток interrupted.
 */
public interface InterfaceInterruptThread {

    /** вызываем когда прерывется поток.
     * @param thread Поток
     */
    void onRemoveThread(ServerThread thread);

    /** Вызываем когда добавляется поток.
     * @param thread Поток
     */
    void onAddThread(ServerThread thread);
}
