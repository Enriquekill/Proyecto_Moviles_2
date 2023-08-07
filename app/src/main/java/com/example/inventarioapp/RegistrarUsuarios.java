package com.example.inventarioapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrarUsuarios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarUsuarios extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tv_nombre, tv_pass, tv_email, tv_dir, tv_rol;
    private Button btn_guardar;
    private static final String PREFS_NAME = "MyPrefsFile";


    public RegistrarUsuarios() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrarUsuarios.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrarUsuarios newInstance(String param1, String param2) {
        RegistrarUsuarios fragment = new RegistrarUsuarios();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_registrar_usuarios, container, false);

        tv_nombre = view.findViewById(R.id.tv_nombre);
        tv_pass = view.findViewById(R.id.tv_pass);
        tv_email = view.findViewById(R.id.tv_email);
        tv_dir = view.findViewById(R.id.tv_dir);
        tv_rol = view.findViewById(R.id.tv_rol);

        btn_guardar = view.findViewById(R.id.btn_guardar);

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_nombre.getText().toString().length() == 0 || tv_pass.getText().toString().length() == 0
                        || tv_email.getText().toString().length() == 0 || tv_dir.getText().toString().length() == 0 || tv_rol.getText().toString().length() == 0){

                    if(tv_nombre.getText().toString().length() == 0){
                        Toast.makeText(getActivity(), "Ingrese un Nombre", Toast.LENGTH_SHORT).show();
                    }else if(tv_pass.getText().toString().length() == 0){
                        Toast.makeText(getActivity(), "Ingrese un Contraseña", Toast.LENGTH_SHORT).show();
                    }else if(tv_email.getText().toString().length() == 0 ){
                        Toast.makeText(getActivity(), "Ingrese un Correo", Toast.LENGTH_SHORT).show();
                    }else if(tv_dir.getText().toString().length() == 0){
                        Toast.makeText(getActivity(), "Ingrese un Dirección", Toast.LENGTH_SHORT).show();
                    }else if(tv_rol.getText().toString().length() == 0){
                        Toast.makeText(getActivity(), "Ingrese un Rol", Toast.LENGTH_SHORT).show();
                    }


                }else{
                    guardarPerfil();
                }
            }
        });

        return view;
    }

    public void guardarPerfil(){

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "https://ceruminous-helmsman.000webhostapp.com/registrar_usuario.php";

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String message = new String(response.data);
                System.out.println(message);

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(message);
                    String status = jsonResponse.getString("status");
                    if(status.equals("success")){
                        Toast.makeText(getActivity(), "Usuario Guardado", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getActivity(), "Error Status False", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                Toast.makeText(getActivity(), "Error Intentelo más tarde", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("correo", tv_email.getText().toString());
                params.put("nombre", tv_nombre.getText().toString());
                params.put("password", tv_pass.getText().toString());
                params.put("rol", tv_rol.getText().toString());

                try {
                    String direccion = tv_dir.getText().toString();
                    params.put("dire", URLEncoder.encode(direccion, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                return params;
            }
        };

        queue.add(request);
    }

}