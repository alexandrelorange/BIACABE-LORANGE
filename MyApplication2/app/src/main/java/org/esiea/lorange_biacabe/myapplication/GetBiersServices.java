package org.esiea.lorange_biacabe.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;



public class GetBiersServices extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GET_ALL_BIERS = "org.esiea.coffin_royledoux.myapplication.action.GET_ALL_BIERS";
    private static final String TAG ="GetBiersServices";
    public static final String BIERS_UPDATE ="com.octip.cours.INF4042_11.BIERS_UPDATE";

    // TODO: Customize helper method
    public static void startActionGet_All_Biers(Context context) {
        Intent intent = new Intent(context, GetBiersServices.class);
        intent.setAction(ACTION_GET_ALL_BIERS);
        context.startService(intent);
    }

    public GetBiersServices() {
        super("GetBiersServices");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_ALL_BIERS.equals(action)) {
                handleActionGet_All_Biers();
            }
        }
    }

    private void copyInputStreamToFile(InputStream in, File file){
        try{
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleActionGet_All_Biers() {
        // TODO: Handle action Get_All_Biers
        Log.v(TAG,"Thread service name:" + Thread.currentThread().getName());
        URL url=null;
        try {
            url = new URL("http://binouze.fabrigli.fr/bieres.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "bieres.json"));

                Log.d(TAG,"Bieres json downloaded !");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SecondActivity.BIERS_UPDATE));

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

