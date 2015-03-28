package com.example.enduser.myapplication;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CondicionesClimaticasActivity extends ActionBarActivity {
JSONObject jsonObject;
    private TextView ciudadTextView;
    private TextView temperturaTextView;
    private TextView tempMaxTextView;
    private TextView tempMinTextView;
    private EditText humedadTextView;
    private EditText nubosidadTextView;
    private EditText vientoTextView;
    private TextView condicionTextView;
    private TextView condicionDescripcionTextView;
    RequestQueue requestQueue;
    ImageRequest request;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condiciones_climaticas);
        requestQueue = Volley.newRequestQueue(this);

        /*TODO recuperar cadena JSon de los extras*/
        String s=getIntent().getExtras().getString(MainActivity.JSON_OBJECT_WEATHER);

        try {
            /*Todo convertir a objeto json*/
            jsonObject= new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*TODO recuperar views de la interfaz de usuario */
        ciudadTextView= (TextView) findViewById(R.id.textView_ciudad);
        humedadTextView = (EditText)findViewById(R.id.textView_humedad);
        nubosidadTextView = (EditText)findViewById(R.id.textView_nubosidad);
        temperturaTextView= (TextView) findViewById(R.id.textView_temperatura_actual);
        tempMaxTextView= (TextView) findViewById(R.id.textView_temperatura_maxima);
        tempMinTextView= (TextView) findViewById(R.id.textView_temperatura_minima);
        vientoTextView = (EditText) findViewById(R.id.textView_viento);
        condicionTextView = (TextView) findViewById(R.id.condicion);
        condicionDescripcionTextView = (TextView) findViewById(R.id.condicion_descripcion);
        image= (ImageView)findViewById(R.id.imageView);

        /*TODO declarar metodo setJson*/
        try {
            setJsonObject(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setJsonObject(JSONObject jsonObject) throws JSONException {
        String name =jsonObject.getString("name");
        ciudadTextView.setText(name);
        JSONObject main = jsonObject.getJSONObject("main");
        humedadTextView.setText(main.getString("humidity")+"%");
        temperturaTextView.setText(main.getString("temp")+" K");
        tempMaxTextView.setText("max "+ main.getString("temp_max")+" K");
        tempMinTextView.setText("min "+ main.getString("temp_min")+" K");
        JSONObject wind  = jsonObject.getJSONObject("wind");
        vientoTextView.setText(wind.getString("speed")+"mph");

        JSONObject clouds = jsonObject.getJSONObject("clouds");
        nubosidadTextView.setText(clouds.getString("all")+"%");
        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        JSONObject weatherObject = (JSONObject)weatherArray.get(0);
        condicionTextView.setText(weatherObject.getString("main"));
        condicionDescripcionTextView.setText(weatherObject.getString("description"));
        setImage(weatherObject.getString("icon"));

    }

    private void setImage(String icon) {
        String url = "http://openweathermap.org/img/w/"+icon+".png";
    request =new ImageRequest(url,new Response.Listener<Bitmap>() {
        @Override
        public void onResponse(Bitmap response) {
           image.setImageBitmap(response);
        }
    },0,0,null,new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            image.setImageResource(R.mipmap.ic_launcher);
        }
    });
        requestQueue.add(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_condiciones_climaticas, menu);
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
}
