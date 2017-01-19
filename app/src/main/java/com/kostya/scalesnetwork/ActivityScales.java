package com.kostya.scalesnetwork;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.*;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.AdRequest;
import com.kostya.scalesnetwork.settings.ActivityPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

/**
 * @author Kostya
 */
public class ActivityScales extends AppCompatActivity implements View.OnClickListener{
    //private Toolbar mToolbar;
    private ImageView buttonBack;
    private TextView textViewWeight ;
    private Receiver receiver;
    private InterstitialAd mInterstitialAd;
    private SpannableStringBuilder textKg;
    private static final int FILE_SELECT_CODE = 10;
    private static  final String TAG = ActivityScales.class.getName();
    public static final String WEIGHT = "com.kostya.scaleswifinet.WEIGHT";


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        setContentView(R.layout.activity_main);

        //getSupportActionBar().hide();
        //mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(mToolbar);
        /*findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            //@TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                mToolbar.showOverflowMenu();
            }
        });*/
        //MobileAds.initialize(getApplicationContext(), "ca-app-pub-5128519816521867~6355295033");

        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_id));
        mInterstitialAd.setAdListener(new AdListener() {
           /* @Override
            public void onAdLoaded() {
                Toast.makeText(ActivityScales.this,"The interstitial is loaded", Toast.LENGTH_SHORT).show();
                mInterstitialAd.show();
            }*/

            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                //openOptionsMenu();
                //mToolbar.showOverflowMenu();
                startActivity(new Intent(getApplicationContext(), ActivityPreferences.class));
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

        });

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice(Main.getInstance().getDeviceId())
                .build();
        mAdView.loadAd(adRequest);
        requestNewInterstitial();

        buttonBack = (ImageView)findViewById(R.id.buttonBack);
        if(buttonBack != null)
            buttonBack.setOnClickListener(this);

        textKg = new SpannableStringBuilder(getResources().getString(R.string.scales_kg));
        textKg.setSpan(new TextAppearanceSpan(this, R.style.SpanTextKg),0,textKg.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        textViewWeight = (TextView)findViewById(R.id.weightTextView);
        /*textViewWeight.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(intent,0);
                return false;
            }
        });*/

        findViewById(R.id.imageMenu).setOnClickListener(this);

        receiver = new Receiver(getApplicationContext());
        receiver.register();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)// This is for emulators
                .addTestDevice(Main.getInstance().getDeviceId())
                .build();

        mInterstitialAd.loadAd(adRequest);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonBack:
                onBackPressed();
                break;
            case R.id.imageMenu:
                //mToolbar.showOverflowMenu();
                /*if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    //openOptionsMenu();
                    mToolbar.showOverflowMenu();
                }*/
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiver.unregister();
    }

    /*@Override
    public void openOptionsMenu() {
        //super.openOptionsMenu();
        Configuration config = getResources().getConfiguration();
        if ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) > Configuration.SCREENLAYOUT_SIZE_LARGE) {
            int originalScreenLayout = config.screenLayout;
            config.screenLayout = Configuration.SCREENLAYOUT_SIZE_LARGE;
            super.openOptionsMenu();
            config.screenLayout = originalScreenLayout;
        } else {
            super.openOptionsMenu();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences:
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    //mToolbar.showOverflowMenu();
                    startActivity(new Intent(this, ActivityPreferences.class));
                }
                break;
            case R.id.exit:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle(getString(R.string.scale_off));
                dialog.setCancelable(false);
                dialog.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_POSITIVE) {
                            //todo сделать что то для выключения весов
                            finish();
                        }
                    }
                });
                dialog.setNegativeButton(getString(R.string.Close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
                dialog.setMessage(getString(R.string.TEXT_MESSAGE));
                dialog.show();
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
        //return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri);
                    // Get the path
                    String path = uri.getPath();
                    //String path = File.getPath(this, uri);
                    Log.d(TAG, "File Path: " + path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",  Toast.LENGTH_SHORT).show();
        }
    }

    class Receiver extends BroadcastReceiver {
        final Context mContext;
        final IntentFilter filter;
        protected boolean isRegistered;

        Receiver(Context context){
            mContext = context;
            filter = new IntentFilter(WEIGHT);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(WEIGHT)) {
                String weight = intent.getStringExtra("weight");
                weight.trim();
                try {
                    //weight = weight.substring(0,weight.indexOf("("));
                    SpannableStringBuilder spannableWeightText = new SpannableStringBuilder(weight);
                    spannableWeightText.setSpan(new TextAppearanceSpan(ActivityScales.this, R.style.SpanTextWeight),0,weight.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    spannableWeightText.append(textKg);
                    textViewWeight.setText(spannableWeightText, TextView.BufferType.SPANNABLE);
                }catch (Exception e){}
            }
        }

        public void register() {
            isRegistered = true;
            mContext.registerReceiver(this, filter);
        }

        public void unregister() {
            if (isRegistered) {
                mContext.unregisterReceiver(this);  // edited
                isRegistered = false;
            }
        }
    }

}
