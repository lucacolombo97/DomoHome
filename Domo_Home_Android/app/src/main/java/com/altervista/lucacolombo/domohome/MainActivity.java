package com.altervista.lucacolombo.domohome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    boolean allarme = false;
    boolean cancello = false;
    boolean box = false;
    boolean led = false;
    boolean temp = false;
    boolean rgb = false;

    Switch switchAllarme;
    Switch switchTemp;
    Switch switchLedSala;
    Switch switchCancello;
    Switch switchBox;
    TextView getTemp;
    TextView setRGB;
    TextView setTemp;
    SharedPreferences loggato;

    String[] atmosfera = {"Off", "Passionale", "Allegra", "Concentrazione", "Relax", "Calma"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loggato = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = loggato.edit();
        editor.putBoolean("Login", true);
        editor.apply();

        switchAllarme = (Switch) findViewById(R.id.switchAllarme);
        switchTemp =(Switch) findViewById(R.id.switchSetTemperatura);
        switchLedSala = (Switch) findViewById(R.id.switchLedSala);
        switchCancello = (Switch) findViewById(R.id.switchCancello);
        switchBox = (Switch) findViewById(R.id.switchBox);
        getTemp = (TextView) findViewById(R.id.textViewTempAttuale);
        setRGB = (TextView) findViewById(R.id.textViewSetRGB);
        setTemp = (TextView) findViewById(R.id.textViewSetTemp);

        controllaAllarme(switchAllarme);

        controllaTemperatura(setTemp);

        controllaLed(switchLedSala);

        controllaCancello(switchCancello);

        controllaBox(switchBox);

        controllaRGB(setRGB);

        assert switchAllarme != null;
        switchAllarme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    if (allarme) {
                        switchBox.setChecked(false);
                        switchCancello.setChecked(false);
                    }
                    switchBox.setEnabled(false);
                    switchCancello.setEnabled(false);
                } else {
                    switchBox.setEnabled(true);
                    switchCancello.setEnabled(true);
                }
                if (allarme)
                    comandiAllarme(isChecked);
            }
        });

        switchTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (temp) {

                    if (isChecked) {
                        setTemp.setEnabled(true);
                        setTemp.callOnClick();
                    } else {
                        setTemp.setText("Off");
                        setTemp.setEnabled(false);
                        spegniRaffreddamento();
                    }
                }
            }
        });

        assert setTemp != null;
        setTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Seleziona valore temperatura: ");
                final NumberPicker np = new NumberPicker(MainActivity.this);

                np.setMinValue(10);
                np.setMaxValue(30);
                np.setWrapSelectorWheel(false);
                np.setValue(20);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String valore = Integer.toString(np.getValue());
                        setTemp.setText(valore);
                        if (temp)
                            setTemperatura(valore);
                    }
                });
                alert.setView(np);
                alert.show();
            }
        });
        assert setRGB != null;
        setRGB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Seleziona atmosfera: ");
                final NumberPicker picker = new NumberPicker(MainActivity.this);
                picker.setMinValue(0);
                picker.setMaxValue(5);
                picker.setWrapSelectorWheel(false);

                picker.setDisplayedValues(atmosfera);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        int stato = 0, red = 0, green = 0, blue = 0;
                        int value = picker.getValue();
                        setRGB.setText(atmosfera[value]);
                        switch (atmosfera[value]) {
                            case "Off":

                                stato = 0;
                                break;

                            case "Passionale":

                                stato = 1;
                                red = 255;
                                green = 0;
                                blue = 0;
                                break;

                            case "Allegra":

                                stato = 1;
                                red = 255;
                                green = 165;
                                blue = 0;
                                break;

                            case "Concentrazione":

                                stato = 1;
                                red = 255;
                                green = 255;
                                blue = 102;
                                break;

                            case "Relax":

                                stato = 1;
                                red = 152;
                                green = 255;
                                blue = 152;
                                break;

                            case "Calma":

                                stato = 1;
                                red = 0;
                                green = 127;
                                blue = 255;
                                break;
                        }
                        if (rgb)
                            comandiLedRGB(stato, red, green, blue, atmosfera[value]);

                    }

                });
                alert.setView(picker);
                alert.show();
            }
        });


        assert getTemp != null;
        getTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Aggiorno temperatura", Toast.LENGTH_SHORT).show();
                tempAttuale(getTemp);

            }
        });

        assert switchLedSala != null;
        switchLedSala.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                if (led)
                    comandiLed(isChecked);
            }
        });

        assert switchCancello != null;
        switchCancello.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                if (cancello)
                    comandiCancello(isChecked);
            }
        });

        assert switchBox != null;
        switchBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                if (box)
                    comandiBox(isChecked);
            }
        });
    }

    public void spegniRaffreddamento()
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/set_temperatura_off.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), "Impianto di raffreddamento spento!", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);
    }

    public void comandiAllarme(final boolean isChecked)  //richiesta http per accendere/spegnere allarme
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/set_comandi_allarme.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (isChecked)
                    Toast.makeText(getApplicationContext(), "Allarme attivato!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Allarme disattivato!", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);
    }

    public void comandiLed(final boolean isChecked)  //richiesta http per accendere/spegnere led
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/set_comandi_led.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (isChecked)
                    Toast.makeText(getApplicationContext(), "Luce sala accesa!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Luce sala spenta!", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);
    }

    public void comandiCancello(final boolean isChecked) //richiesta http per aprire/chiudere cancello
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/set_comandi_motore.php?id=2";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (isChecked)
                    Toast.makeText(getApplicationContext(), "Cancello aperto!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Cancello chiuso!", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);
    }

    public void comandiBox(final boolean isChecked) //richiesta http per aprire/chiudere box
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/set_comandi_motore.php?id=1";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (isChecked)
                    Toast.makeText(getApplicationContext(), "Box aperto!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Box chiuso!", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);
    }

    public void tempAttuale(final TextView t) //richiesta http per leggere la temperatura attuale
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/lettura_temperatura_android.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                t.setText(response);
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);
    }

    public void setTemperatura(String valore) //richiesta http per impostare la temperatura
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/set_temperatura_on.php?temperatura="+valore;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(getApplicationContext(), "Temperatura impostata!", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);
    }

    public void comandiLedRGB(int stato,int red,int green, int blue, String atmosfera) //richiesta http per impostare colore led RGB
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/set_comandi_RGB.php?stato="
                +stato+"&red="+red+"&green="+green+"&blue="+blue+"&atmosfera="+atmosfera;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(getApplicationContext(), "Luce camera impostata!", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);
    }

    public void controllaLed(final Switch switchLedSala) //controllo stato iniziale led
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/lettura_comandi_led.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.isEmpty())
                    switchLedSala.setChecked(false);
                else
                    switchLedSala.setChecked(true);

                led=true;
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);

    }

    public void controllaAllarme(final Switch switchAllarme) //controllo stato iniziale allarme
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/lettura_comandi_allarme.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.isEmpty())
                    switchAllarme.setChecked(false);
                else
                    switchAllarme.setChecked(true);

                allarme=true;
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);

    }

    public void controllaBox(final Switch switchBox) //controllo stato iniziale box
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/lettura_comandi_motore_android.php?id=1";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("0"))
                    switchBox.setChecked(false);
                else
                    switchBox.setChecked(true);

                box=true;
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);

    }

    public void controllaCancello(final Switch switchCancello) //controllo stato iniziale cancello
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/lettura_comandi_motore_android.php?id=2";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("0"))
                    switchCancello.setChecked(false);
                else
                    switchCancello.setChecked(true);

                cancello=true;
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);

    }

    public void controllaTemperatura(final TextView setTemp) //controllo temperatura impostata
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/lettura_temperatura_arduino.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                if(response.isEmpty())
                {
                    setTemp.setText("Off");
                    switchTemp.setChecked(false);
                    setTemp.setEnabled(false);
                }
                else
                {
                    switchTemp.setChecked(true);
                    setTemp.setEnabled(true);
                    setTemp.setText(response);

                }
                temp=true;
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);

    }

    public void controllaRGB(final TextView setRGB) //controllo stato iniziale cancello
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/lettura_comandi_RGB_android.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                setRGB.setText(response);
                rgb=true;
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);

    }

    @Override
    protected void onStop()
    {
        super.onStop();

        allarme=false;
        cancello=false;
        box=false;
        led=false;
        temp=false;
        rgb=false;
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();

        controllaAllarme(switchAllarme);
        controllaTemperatura(setTemp);
        controllaLed(switchLedSala);
        controllaCancello(switchCancello);
        controllaBox(switchBox);
        controllaRGB(setRGB);

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        loggato = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = loggato.edit();
        editor.putBoolean("Login", false);
        editor.apply();
    }



}



