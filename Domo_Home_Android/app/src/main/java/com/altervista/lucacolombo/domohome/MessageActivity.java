package com.altervista.lucacolombo.domohome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        AlertDialog.Builder alert = new AlertDialog.Builder(MessageActivity.this);
        alert.setTitle("INTRUSIONE!");
        alert.setMessage("Spegni allarme");
        alert.setCancelable(false);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                spegniAllarme();
                Intent pagina = new Intent(MessageActivity.this, LoginActivity.class);
                startActivity(pagina);
            }
        });
        alert.show();
    }

    public void spegniAllarme()
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/set_sirena_off.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), "Allarme spento!", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);
    }
}
