package com.example.mimo.musiquendo.Provider;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Clase para realizar peticiones personalizadas
 */
public class CustomJSONObject extends Request<JSONObject> {
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    /**
     * Primer constructor de la clase CustomJSONObject
     * @param url Ruta del servicio REST a la que se desea acceder
     * @param params Datos que se necesitan enviar para su procesamiento en el servidor
     * @param reponseListener Listener que recibe la respuesta del servidor
     * @param errorListener Listener que se ejecuta cuando ocurre algún fallo
     */
    public CustomJSONObject(String url, Map<String, String> params,
                            Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public CustomJSONObject(int method, String url, Map<String, String> params,
                            Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.params = params;
    }

    /**
     * Función que permite introducir los datos que se desean enviar a la ruta REST.
     * @return Los parámetros introducidos en forma de clave valor.
     * @throws com.android.volley.AuthFailureError
     */
    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }
}
