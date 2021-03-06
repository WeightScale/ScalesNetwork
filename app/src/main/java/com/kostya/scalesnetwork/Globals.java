package com.kostya.scalesnetwork;
/* Удалил бяку */
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.kostya.scalesnetwork.provider.EventsTable;
import com.kostya.scalesnetwork.provider.SystemTable;
import com.kostya.scalesnetwork.settings.Preferences;
import com.kostya.serializable.ComPortObject;
import com.kostya.serializable.TerminalObject;
import com.kostya.serializable.Terminals;

import java.io.File;

/** Created by Kostya on 23.01.2016.
 * @author Kostya
 */
public class Globals {
    private static Globals instance = new Globals();
    public Terminals terminal;
    public static File pathLocalForms;
    /** Настройки для весов. */
    protected Preferences preferencesScales;
    protected PackageInfo packageInfo;
    private TerminalObject localTerminal;
    //ComPortObject comPortObject;
    /** Версия программы весового модуля. */
    private final int microSoftware = 4;
    protected String networkOperatorName;
    protected String simNumber;
    protected String telephoneNumber;
    protected String networkCountry;
    protected int versionNumber;
    protected String versionName = "";

    private String weight;
    /** Папка для хранения локальных данных программы. */
    public static final String FOLDER_LOCAL_FORMS = "forms";
    private static  final String TAG = Globals.class.getName();
    /** Флаг есть соединение */
    private boolean isScalesConnect;

    public void initialize(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionNumber = packageInfo.versionCode;
            versionName = packageInfo.versionName;
            try {
                terminal = Terminals.values()[Integer.valueOf(new SystemTable(context).getProperty(SystemTable.Name.TERMINAL))];
            } catch (Exception e) {
                terminal = Terminals.DEFAULT;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        /* Создаем путь к временной папке для для хранения файлов. */
        //pathLocalForms = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + FOLDER_LOCAL_FORMS);
        /* Создаем путь к папке для для хранения файлов с данными формы google disk form. */
        pathLocalForms = new File(context.getFilesDir() + File.separator + FOLDER_LOCAL_FORMS);
        /* Если нет папки тогда создаем. */
        if (!pathLocalForms.exists()) {
            if (!pathLocalForms.mkdirs()) {
                new EventsTable(context).insertNewEvent("Путь не созданый: " + pathLocalForms.getPath(), EventsTable.Event.PATH_STORE);
            }
        }
        /* Создаем локальный весовой терминал. */
        int t = Integer.valueOf(new SystemTable(context).getProperty(SystemTable.Name.TERMINAL, String.valueOf(Terminals.DEFAULT.ordinal())));
        localTerminal = new TerminalObject(Terminals.values()[t]);
        /* Извлекаем настройки из базы данных. */
        SystemTable systemTable = new SystemTable(context);
        ComPortObject comPortObject = new ComPortObject();
        comPortObject.setSpeed(Integer.valueOf(systemTable.getProperty(SystemTable.Name.SPEED_PORT, "9600")));
        comPortObject.setDataBits(ComPortObject.usbProperties.get(systemTable.getProperty(SystemTable.Name.FRAME_PORT, "8")));
        comPortObject.setStopBits(ComPortObject.usbProperties.get(systemTable.getProperty(SystemTable.Name.STOP_BIT, "1")));
        comPortObject.setParity(ComPortObject.usbProperties.get(systemTable.getProperty(SystemTable.Name.PARITY_BIT, "none")));
        comPortObject.setFlowControl(ComPortObject.usbProperties.get(systemTable.getProperty(SystemTable.Name.FLOW_CONTROL, "OFF")));

        /* Добавляем сохраненные настройки порта. */
        localTerminal.setComPortObject(comPortObject);


    }

    public boolean isScalesConnect() {
        return isScalesConnect;
    }

    public void setScalesConnect(boolean connect) {
        isScalesConnect = connect;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getNetworkOperatorName() {
        return networkOperatorName;
    }

    public void setNetworkOperatorName(String networkOperatorName) {
        this.networkOperatorName = networkOperatorName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public void setNetworkCountry(String networkCountry) {
        this.networkCountry = networkCountry;
    }

    public int getMicroSoftware() { return microSoftware; }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Preferences getPreferencesScales() { return preferencesScales; }

    public static Globals getInstance() { return instance; }

    public static void setInstance(Globals instance) { Globals.instance = instance; }

    public TerminalObject getLocalTerminal() {return localTerminal;}
}
