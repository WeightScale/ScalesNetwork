package com.kostya.scalesnetwork.settings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.WindowManager;
import android.widget.TextView;
import com.kostya.scalesnetwork.Globals;
import com.kostya.scalesnetwork.R;
import com.kostya.scalesnetwork.provider.SystemTable;

import java.util.List;

/*
 * Created by Kostya on 26.04.14.
 */
public class ActivityAbout extends Activity {
    private static Context mContext;
    private static Globals globals;
    enum StrokeSettings{
        VERSION(R.string.Version_scale){
            @Override
            String getValue() {
                return String.valueOf(globals.getVersionNumber()); }

            @Override
            int getMeasure() { return -1; }
        },
        NAME_TERMINAL(R.string.Name_terminal) {
            @Override
            String getValue() {return globals.terminal.name(); }

            @Override
            int getMeasure() { return -1;}
        },
        NAME_NETWORK(R.string.Name_network) {
            @Override
            String getValue() {
                try {
                    return '"' + getNameOfId(mContext, Integer.valueOf(new SystemTable(mContext).getProperty(SystemTable.Name.WIFI_DEFAULT))) + '"' + '\n';
                } catch (Exception e) {
                    return "";
                }
            }

            @Override
            int getMeasure() { return -1; }

            String getNameOfId(Context context, int id){
                List<WifiConfiguration> list = ((WifiManager)context.getSystemService(Context.WIFI_SERVICE)).getConfiguredNetworks();
                for (WifiConfiguration wifiConfiguration : list){
                    if (wifiConfiguration.networkId == id){
                        return  wifiConfiguration.SSID.replace("\"", "");
                    }
                }
                return "";
            }
        }
        /*BATTERY(R.string.Battery) {
            @Override
            String getValue() { return globals.getBattery() + " %"; }

            @Override
            int getMeasure() { return -1; }
        },
        TEMPERATURE(R.string.Temperature) {
            @Override
            String getValue() {
                String temp;
                try {
                    temp = scaleModule.getTemperature() + "°" + 'C';
                }catch (Exception e){
                    temp = "error"+ '\n';
                }
                return temp;
            }

            @Override
            int getMeasure() { return -1; }
        },*/
        /*COEFFICIENT_A(R.string.Coefficient) {
            @Override
            String getValue() {  return String.valueOf(scaleModule.getCoefficientA()); }

            @Override
            int getMeasure() { return -1; }
        },*/
        /*WEIGHT_MAX(R.string.MLW) {
            final int resIdKg = R.string.scales_kg;
            @Override
            String getValue() {  return scaleModule.getWeightMax() + " "; }

            @Override
            int getMeasure() { return resIdKg; }
        },
        TIME_OFF(R.string.Off_timer) {
            final int reIdMinute = R.string.minute;
            @Override
            String getValue() { return scaleModule.getTimeOff() + " "; }

            @Override
            int getMeasure() { return reIdMinute; }
        },
        STEP(R.string.Step_capacity_scale){
            final int resIdKg = R.string.scales_kg;
            @Override
            String getValue() { return scaleModule.getStepScale() + " "; }

            @Override
            int getMeasure() {  return resIdKg; }
        }*/;

        private final int resId;
        abstract String getValue();
        abstract int getMeasure();

        StrokeSettings(int res){
            resId = res;
        }

        public int getResId() {return resId;}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        setTitle(getString(R.string.About));

        mContext = this;
        globals = Globals.getInstance();

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 1.0f;
        getWindow().setAttributes(lp);

        TextView textSoftVersion = (TextView) findViewById(R.id.textSoftVersion);
        textSoftVersion.setText(globals.getPackageInfo().versionName + ' ' + String.valueOf(globals.getPackageInfo().versionCode));

        TextView textSettings = (TextView) findViewById(R.id.textSettings);
        parserTextSettings(textSettings);
        textSettings.append("\n");

        TextView textAuthority = (TextView) findViewById(R.id.textAuthority);
        textAuthority.append(getString(R.string.Copyright) + '\n');
        textAuthority.append(getString(R.string.Reserved) + '\n');
    }

    void parserTextSettings(TextView textView){
        for (StrokeSettings s : StrokeSettings.values()){
            try {
                SpannableStringBuilder text = new SpannableStringBuilder(getString(s.getResId()));
                text.setSpan(new StyleSpan(Typeface.NORMAL), 0, text.length(), Spanned.SPAN_MARK_MARK);
                textView.append(text);
                SpannableStringBuilder value = new SpannableStringBuilder(s.getValue());
                value.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),0,value.length(), Spanned.SPAN_MARK_MARK);
                textView.append(value);
                textView.append((s.getMeasure() == -1 ? "" : getString(s.getMeasure())) + '\n');
            }catch (Exception e){
                textView.append("\n");
            }
        }
    }
}
