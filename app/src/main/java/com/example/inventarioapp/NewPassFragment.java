package com.example.inventarioapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewPassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPassFragment extends Fragment {

    EditText txtPass, txtCPass, tv_email;
    String user;
    Button btnReset, btnCancel;
    View mView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewPassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewPass.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPassFragment newInstance(String param1, String param2) {
        NewPassFragment fragment = new NewPassFragment();
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

        mView = inflater.inflate(R.layout.fragment_new_pass, container, false);

        tv_email = mView.findViewById(R.id.tv_email);
        txtPass = mView.findViewById(R.id.inCorreo2);
        txtCPass = mView.findViewById(R.id.inCorreo3);

        btnReset = mView.findViewById(R.id.btnInicioSesion2);
        btnCancel = mView.findViewById(R.id.btnCancelar);

        Bundle data = getArguments();
        user = data.getString("user");

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtPass.getText().toString().isEmpty()) {
                    mostrarError("No se ha ingresado la contraseña");
                    return;
                }
                if(txtCPass.getText().toString().isEmpty()) {
                    mostrarError("No se ha ingresado la confirmación de la contraseña");
                    return;
                }
                if(!txtPass.getText().toString().equals(txtCPass.getText().toString())) {
                    mostrarError("Las contraseñas no coinsiden");
                    return;
                }

                validarUsuario("https://ceruminous-helmsman.000webhostapp.com/reset_account.php");
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return mView;
    }

    private void mostrarError(String msg){
        Snackbar snackbar = Snackbar.make(mView, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void validarUsuario(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mostrarError("Contraseña cambiada correctamente");
                getActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mostrarError(error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("correo", tv_email.getText().toString());
                params.put("nueva_contraseña", txtPass.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}