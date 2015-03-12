package uf1.infobosccoma.mymapllocsdinteres.Model;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uf1.infobosccoma.mymapllocsdinteres.Controller.LlocInteres;
import uf1.infobosccoma.mymapllocsdinteres.R;

public class MapsActivity extends FragmentActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private DescarregarDades download;
    private Spinner spinner;
    private static final String URL_DATA = "http://www.infobosccoma.net/pmdm/pois.php";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ArrayList<LlocInteres> dades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        Button btnCercar= (Button) findViewById(R.id.btnCenter);
        btnCercar.setOnClickListener(this);
        spinner=(Spinner)findViewById(R.id.spinnerMaps);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

   private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                //setUpMap();
            }
        }
    }

   private void setUpMap() {
      /* LocationManager mlocManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
       UbicacioActual ubicacioListener= new UbicacioActual();
       ubicacioListener.setActivitat(this);
       mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,(UbicacioActual)ubicacioListener);
       LatLng lng = new LatLng(ubicacioListener.getLatitude(),ubicacioListener.getLongitude());
       mMap.addMarker(new MarkerOptions()
               .position(lng)
               .title("La meva ubicacio actual")
       );*/
    }

    @Override
    public void onClick(View v) {
        mMap.clear();
        download= new DescarregarDades();
        download.execute(URL_DATA);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String tipus=(String) parent.getItemAtPosition(position);
        if(position==0){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }else if(position==1){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }else if(position==2){
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }else if(position==3){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class DescarregarDades extends AsyncTask<String, Void, ArrayList<LlocInteres>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<LlocInteres> doInBackground(String... params) {
            EditText etCercador= (EditText)findViewById(R.id.etCercador);
            ArrayList<LlocInteres> llistaTitulars = null;
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppostreq = new HttpPost(URL_DATA);
            HttpResponse httpresponse = null;
            try {
                if(etCercador.equals("")) {
                    httpresponse = httpclient.execute(httppostreq);
                    String responseText = EntityUtils.toString(httpresponse.getEntity());
                    llistaTitulars = tractarJSON(responseText);
                }else{
                    List<NameValuePair> parametres = new ArrayList<NameValuePair>(2);
                    parametres.add(new BasicNameValuePair("city", etCercador.getText().toString()));
                    httppostreq.setEntity(new UrlEncodedFormEntity(parametres));
                    httpresponse = httpclient.execute(httppostreq);
                    String responseText = EntityUtils.toString(httpresponse.getEntity());
                    llistaTitulars = tractarJSON(responseText);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return llistaTitulars;
        }

        @Override
        protected void onPostExecute(ArrayList<LlocInteres> llista) {
            dades = llista;
            LatLngBounds.Builder bounds = new LatLngBounds.Builder();
            if (llista.size()!=0) {
                for (int i = 0; i < llista.size(); i++) {
                    LatLng latitudeIlongitude = new LatLng(llista.get(i).getLatitude(), llista.get(i).getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(latitudeIlongitude)
                            .title(llista.get(i).getName())
                            .snippet(llista.get(i).getCity() + ", " + llista.get(i).getLatitude()+", "+llista.get(i).getLongitude()));
                    bounds.include(latitudeIlongitude);
                }
                LatLngBounds boundsZoom = bounds.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsZoom, 150));
            }
        }


        private ArrayList<LlocInteres> tractarJSON(String json) {
            Gson converter = new Gson();
            return converter.fromJson(json, new TypeToken<ArrayList<LlocInteres>>(){}.getType());
        }

    }
}
