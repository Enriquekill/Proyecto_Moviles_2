package com.example.inventarioapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemoveInventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemoveInventoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText et_producto, et_cantidad;
    public RemoveInventoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RemoveInventoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RemoveInventoryFragment newInstance(String param1, String param2) {
        RemoveInventoryFragment fragment = new RemoveInventoryFragment();
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

        View view = inflater.inflate(R.layout.fragment_remove_inventory, container, false);

        et_producto = (EditText) view.findViewById(R.id.et_producto);
        et_cantidad =(EditText) view.findViewById(R.id.et_cantidad);


        Button btnAceptar = (Button) view.findViewById(R.id.btnAceptar);


        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_producto.getText().length() == 0 || et_cantidad.getText().length() == 0){
                    Toast.makeText(getActivity(), "Ingrese el Nombre y Cantidad", Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            quitarProductos();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        return view;
    }

    public void quitarProductos(){

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "https://ceruminous-helmsman.000webhostapp.com/desminuirProducto.php";

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String message = new String(response.data);
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(message);
                    String status = jsonResponse.getString("status");
                    if(status.equals("success")){
                        Toast.makeText(getActivity(), "Operación Realizada", Toast.LENGTH_SHORT).show();

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
                params.put("producto", et_producto.getText().toString());
                params.put("cantidad", String.valueOf(Integer.parseInt(et_cantidad.getText().toString())));
                return params;
            }
        };

        queue.add(request);
    }


}