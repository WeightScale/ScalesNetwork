package com.kostya.scalesnetwork.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import com.kostya.scalesnetwork.R;
import com.kostya.serializable.Terminals;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kostya on 26.06.2016.
 */
public class ListPreferenceTerminals extends ListPreference {
    private int mClickedDialogEntryIndex;
    final List<Terminals> terminals = Arrays.asList(Terminals.values());
    //List<ScanResult> scanResultList;


    public ListPreferenceTerminals(Context context, AttributeSet attrs) {
        super(context, attrs);
        //WifiConfiguration wifiConfiguration = new WifiConfiguration();
        //WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //list = wifiManager.getConfiguredNetworks();
        //scanResultList = wifiManager.getScanResults();
        /*entries = new CharSequence[list.size()];
        entryValues = new CharSequence[list.size()];
        int i = 0;
        for (WifiConfiguration wifiConfiguration : list){
            entries[i] = wifiConfiguration.SSID;
            entryValues[i] = String.valueOf(wifiConfiguration.networkId);
            i++;
        }
        setEntries(entries);
        setEntryValues(entryValues);*/
        setPersistent(true);

        mClickedDialogEntryIndex = getPersistedInt(0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        int value = restoreValue? getPersistedInt(mClickedDialogEntryIndex) : (Integer) defaultValue;
        setValue(value);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
       if (positiveResult && mClickedDialogEntryIndex >= 0 /*&& entryValues != null*/) {
            Terminals value = terminals.get(mClickedDialogEntryIndex);
            if (callChangeListener(value)) {
                setValue(mClickedDialogEntryIndex);
            }
        }
    }

    public void setValue(int value) {
        if (shouldPersist()) {
            persistInt(value);
        }
        if (value != mClickedDialogEntryIndex) {
            mClickedDialogEntryIndex = value;
            notifyChanged();
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
    }

    @Override
    protected void onPrepareDialogBuilder( AlertDialog.Builder builder ){
        final ListAdapter adapter = new ConfigurationAdapter(getContext(), R.layout.item_list_sender, terminals);

        builder.setSingleChoiceItems(adapter, mClickedDialogEntryIndex, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which ){
                long l = adapter.getItemId( which );
                setValue(which);

                    /*if (mClickedDialogEntryIndex != which) {
                        mClickedDialogEntryIndex = which;
                        if (shouldPersist()) {
                            persistInt(mClickedDialogEntryIndex);
                        }
                        ListPreferenceWifi.this.notifyChanged();
                    }*/
                ListPreferenceTerminals.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);


                dialog.dismiss();
            }
        } );

        builder.setPositiveButton( null, null );

        //setDefaultValue(mClickedDialogEntryIndex);
    }

    class ConfigurationAdapter extends ArrayAdapter<Terminals> {

        public ConfigurationAdapter(Context context, int resource, List<Terminals> list) {
            super(context, resource, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                view = layoutInflater.inflate(R.layout.item_list_sender, parent, false);
            }

            Terminals p = getItem(position);
            CheckedTextView textView = (CheckedTextView) view.findViewById(R.id.text1);
            textView.setText(p.name());
            Terminals t = terminals.get(mClickedDialogEntryIndex);
            if (t == p){
                textView.setTextColor(Color.BLUE);
            }else {
                textView.setTextColor(Color.BLACK);
            }

            /*for (ScanResult scanResult : scanResultList){
                if (scanResult.SSID.equals(wc.SSID.replace("\"",""))){
                    textView.setBackgroundColor(Color.GRAY);
                }else {
                    textView.setBackgroundColor(Color.WHITE);
                }
            }*/

            return view;
        }
    }
}
