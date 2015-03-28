package com.example.enduser.myapplication;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {
    public static final String JSON_OBJECT_WEATHER ="JSON_OBJECT_WEATHER" ;
    /*Url del servicio que se consultará*/
    private final String URL_SERVICIO = "http://api.openweathermap.org/data/2.5/weather";

    private ListView lista;
    /*TODO:1  Agregar etiqueta string-array  con propiedad name ='ciudades' a  archivo strings.xml*/
    private String[] ciudades;
    /*TODO:8 generar el newRequestQueue*/
    RequestQueue requestQueue ;
    /*TODO 9 declarar StringRequest*/
    JsonRequest stringRequest;
    /*TODO 19 Declarar las variables longitud y latitud que utilizaran para consultar el clima de la ubicacion actual*/
    private double latitud;
    private double logitud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue= Volley.newRequestQueue(this);
       /*TODO:2 Recuperar array de ciudades a través de getResources()*/
        ciudades= getResources().getStringArray(R.array.ciudades);
        /*TODO:3 Crear arrayAdapter de Strings */
         /*TODO:4 recuperar el list view*/
         /*TODO:5 asignar adapter*/
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,ciudades);
        lista = (ListView)findViewById(R.id.listView);
        lista.setAdapter(stringArrayAdapter);
        /*TODO:6 asignar listener al ListView*/
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ciudad seleccionada", ciudades[position]);
                stringRequest= new JsonObjectRequest(Request.Method.GET,URL_SERVICIO+"?q="+ciudades[position], new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        iniciarActividad(response);
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG);
                    }
                });
                requestQueue.add(stringRequest);
                /*TODO:6 Importar Libreria volley  Copiar en el archivo build.gradle(Module: app) compile 'com.mcxiaoke.volley:library:1.0.+' en apartado dependencies*/
                /*TODO:7 Agregar permisos de Internet en el AndroidManifest.xml  */
                /*TODO:9 declarar el StringRequest y listener*/
                /*TODO:10 agregar el request a la cola de ejecución de volley  */
                /*TODO:11 Manejar cancelación de peticiones en de volley definir tag y asignarlo al request*/
                /*TODO 13 : Cambiar StringRequest por JSONRequest*/
                /*TODO 14 : Agregar boton en el layout para consultar por GPS*/
                /*TODO 15 : Declarar método que maneje el evento de click del boton de gps y consulte el status del gps */



            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        /*TODO:12 verificar si el request es diferente de null asignarle un tag al request con setTag y cancelarlo de ser así con el método cancellALL(TAG)*/
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onclickGPSButton(View view){
        /*TODO 16 : obtener el LocationManager*/
        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        /*TODO 17 : solicitar los cambios en locacion*/

        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                /*TODO 18 : Recuperar Longitud y Latitud*/
                /*TODO 19 agregar al AndroidManifest.xml uses-permission de gps*/
                logitud = location.getLongitude();
                latitud = location.getLatitude();
                stringRequest = new JsonObjectRequest(Request.Method.GET, URL_SERVICIO + "?lat=" + latitud + "&lon=" + logitud, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            /*TODO 21 declaramos metodo que recibe el JSONObject e inicia la nueva actividad */
                        iniciarActividad(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG);
                    }
                });
                requestQueue.add(stringRequest);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener );

    }

    private void iniciarActividad(JSONObject response) {
        Intent intent = new Intent(getApplicationContext(),CondicionesClimaticasActivity.class);
                            /*TODO 22 declarar la constante estatica con la que referenciaremos el objeto Json*/
        intent.putExtra(JSON_OBJECT_WEATHER,response.toString());
        startActivity(intent);
    }
}
/*TODO 20 creamos nueva actividad DisplayInformationActivity*/