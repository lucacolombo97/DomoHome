package com.altervista.lucacolombo.domohome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    ProgressDialog dialog=null;
    String SENDER_ID = "824909131832";

    GoogleCloudMessaging gcm;
    Context context;
    String regid;
    SharedPreferences loggato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText textUser = (EditText) findViewById(R.id.editTextUsername);
        final EditText textPsw = (EditText) findViewById(R.id.editTextPassword);
        loginBtn = (Button) findViewById(R.id.buttonLogin);
        final RelativeLayout loginLayout = (RelativeLayout) findViewById(R.id.loginLayout);

        context=this;
        gcm = GoogleCloudMessaging.getInstance(this);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        loggato = PreferenceManager.getDefaultSharedPreferences(this);

        if (!pref.getBoolean("primaEsecuzione", false))
        {
            registrazioneGCM();
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("primaEsecuzione", true);
            editor.apply();
        }

        if (loggato.getBoolean("Login", false))
        {
            Intent pagina = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(pagina);
        }

        assert loginBtn != null;
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog=ProgressDialog.show(context, "Log in", "Tentativo in corso...", true, false);
                final String user = textUser.getText().toString();
                final String psw = textPsw.getText().toString();
                loginBtn.setEnabled(false);
                logIn(user, psw);
            }
        });

        assert loginLayout != null;
        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textUser.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(textPsw.getWindowToken(), 0);
            }
        });

    }

    public void logIn(final String user, final String psw) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://lucacolombo.altervista.org/Domo_Home/login.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("ok")) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Login effettuato con successo!", Toast.LENGTH_SHORT).show();

                    /*SharedPreferences.Editor editor = loggato.edit();
                    editor.putBoolean("Login", true);
                    editor.apply();*/

                    Intent pagina = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(pagina);
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Credenziali sbagliate!", Toast.LENGTH_SHORT).show();
                    loginBtn.setEnabled(true);
                }
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                        loginBtn.setEnabled(true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", user);
                params.put("psw", psw);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void registrazioneGCM() {
        new AsyncTask<Void, Void, String>()
        {
            private ProgressDialog dialog=null;

            protected void onPreExecute()
            {
                dialog=ProgressDialog.show(context, "Registrazione presso GCM", "Tentativo in corso...", true, false);
            };

            @Override
            protected String doInBackground(Void... params)
            {
                try {
                    if (gcm == null)
                    {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                }
                catch (IOException ex)
                {
                    return null;
                }
                return regid;
            }

            @Override
            protected void onPostExecute(String regid)
            {
                dialog.dismiss();
                if (regid!=null)
                {
                    sendIDToApplication(regid);
                }
                else
                    Toast.makeText(context, "Errore: registrazione su GCM non riuscita!", Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

    private void sendIDToApplication(String regid) {
        new AsyncTask<String, Void, Void>()
        {
            @Override
            protected Void doInBackground(String... params)
            {
                final String regid=params[0];
                String BACKEND_URL="http://lucacolombo.altervista.org/Domo_Home/registra_mobile_push.php?regid="+regid;
                final RequestQueue queue = Volley.newRequestQueue(context);

                final StringRequest stringRequest=new StringRequest(Request.Method.GET, BACKEND_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                        new Response.ErrorListener()
                        {
                            public void onErrorResponse (VolleyError error){

                                Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                            }
                        }){
                };
                queue.add(stringRequest);

                return null;
            }
        }.execute(regid);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        loginBtn.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        loggato = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = loggato.edit();
        editor.putBoolean("Login", false);
        editor.apply();
    }
}
