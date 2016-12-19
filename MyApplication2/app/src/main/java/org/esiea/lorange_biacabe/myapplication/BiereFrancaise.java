package org.esiea.lorange_biacabe.myapplication;

/**
 * Created by datbl on 19/12/2016.
 */
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Action;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class BiereFrancaise extends AppCompatActivity {

    private static final String TAG = "GetBiersServices";
    //public static final String BIERS_UPDATE = "com.octip.cours.INF4042_11.BIERS_UPDATE";
    private RecyclerView rv_bieres = null;
    private JSONArray json;
    //private AlertDialog.Builder ad = null;
    //private AlertDialog alertDialog = null;
    private GoogleApiClient client;
    public static String description;
    public static String nom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biere_francaise);

        GetBiersServices.startActionGet_All_Biers(this);

        rv_bieres = (RecyclerView) findViewById(R.id.rv_bierefr);
        rv_bieres.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        json = getBiersFromFile();
        rv_bieres.setAdapter(new BiersAdapter(json));

        GetBiersServices.startActionGet_All_Biers(this);

        IntentFilter intentFilter = new IntentFilter(BIERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BiersUpdate(), intentFilter);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent tIntent = new Intent(this, MainActivity.class);
            startActivity(tIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static final String BIERS_UPDATE = "com.octip.inf4042_11.BIERS_UPDATE";

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.esiea.coffin_royledoux.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.esiea.coffin_royledoux.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class BiersUpdate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());
            //Ajouter une notification
            Toast.makeText(getApplicationContext(), R.string.dwld, Toast.LENGTH_SHORT).show();
            rv_bieres.setAdapter(new BiersAdapter(getBiersFromFile()));
        }
    }

    public JSONArray getBiersFromFile() {
        try {
            InputStream is = new FileInputStream(getCacheDir() + "/" + "bieres.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private class BiersAdapter extends RecyclerView.Adapter<BiersAdapter.BierHolder> {
        private JSONArray biers;

        public BiersAdapter(JSONArray jsonarray) {
            this.biers = jsonarray;
        }

        @Override
        public BierHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            LayoutInflater li = LayoutInflater.from(viewGroup.getContext());

            View v = li.inflate(R.layout.content_biere_francaise, viewGroup, false);

            return new BierHolder(v);
        }

        @Override
        public void onBindViewHolder(BierHolder bierHolder, int i) {

            try{
                final JSONObject jObj= biers.getJSONObject(i);


                if (jObj.getString("country_id").equals("12") ) {

                    String jS = jObj.getString("name");
                    bierHolder.name.setText(jS);
                    bierHolder.name.setVisibility(View.VISIBLE);
                    bierHolder.name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getApplicationContext(), getString(R.string.msg), Toast.LENGTH_LONG).show();
                            show_description(jObj);
                        }
                    });
                }else{
                    bierHolder.name.setVisibility(View.GONE);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return biers.length();
        }

        class BierHolder extends RecyclerView.ViewHolder {

            Button name;


            public BierHolder(View itemView) {
                super(itemView);
                name = (Button) itemView.findViewById(R.id.rv_bier_element_name_fr);
            }
        }
    }

    private void show_description(JSONObject biere){
        //Intent tIntent = new Intent(this, MainActivity.class);
        //startActivity(tIntent);
        try {
            description = biere.getString("description");
            nom = biere.getString("name");
            InformationFragment_Fr informationFragment = new InformationFragment_Fr();
            informationFragment.show(getSupportFragmentManager().beginTransaction(), "dialog_information");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
