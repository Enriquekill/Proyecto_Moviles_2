package com.example.inventarioapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout productosContenedor;

    public InventoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InventoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InventoryFragment newInstance(String param1, String param2) {
        InventoryFragment fragment = new InventoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        productosContenedor = view.findViewById(R.id.productosContenedor);

        obtenerInfo();
        return view;

    }


    public void obtenerInfo() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "https://ceruminous-helmsman.000webhostapp.com/obtenerProductos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status.toString().equals("success")) {
                                JSONArray res = jsonObject.getJSONArray("data");
                                productosContenedor.removeAllViews();

                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject objeto = res.getJSONObject(i);

                                    String nombre = objeto.getString("nombre");
                                    String cantidad = objeto.getString("cantidad");
                                    String id = objeto.getString("id");

                                    agregar(nombre, cantidad, id);
                                }


                            }else if(status.toString().equals("false")){
                                productosContenedor.removeAllViews();
                            }

                        } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error al hacer la petición", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error al hacer la petición", Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }

    private void agregar(String nombre, String cantidad, String idProducto){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        int id = R.layout.producto;
        LinearLayout relativeLayout = (LinearLayout) inflater.inflate(id, null, false);

        TextView et_producto = (TextView) relativeLayout.findViewById(R.id.tv_producto);
        TextView et_cantidad = (TextView) relativeLayout.findViewById(R.id.tv_cantidad);

        ImageButton del = (ImageButton) relativeLayout.findViewById(R.id.btnDel);

        et_producto.setText(nombre);
        et_cantidad.setText(cantidad);
        del.setTag(idProducto);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Crear un constructor de AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                // Personalizar el AlertDialog
                builder.setCancelable(true); // Permite cerrar el AlertDialog haciendo clic fuera de él
                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aquí se ejecuta la acción de eliminar si el usuario hace clic en el botón "Eliminar"
                        // Puedes poner aquí la lógica para eliminar el elemento
                        eliminarProducto(view.getTag().toString());
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aquí se ejecuta la acción de cancelar si el usuario hace clic en el botón "Cancelar"
                        // Cerrar el AlertDialog
                        dialog.dismiss();
                    }
                });

                // Crear y mostrar el AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(params);
        productosContenedor.addView(relativeLayout);

    }

    public void eliminarProducto(String id){

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "https://ceruminous-helmsman.000webhostapp.com/eliminarProducto.php";

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String message = new String(response.data);
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(message);
                    String status = jsonResponse.getString("status");
                    if(status.equals("success")){
                        obtenerInfo();
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
                params.put("id", id);
                return params;
            }
        };

        queue.add(request);
    }


}