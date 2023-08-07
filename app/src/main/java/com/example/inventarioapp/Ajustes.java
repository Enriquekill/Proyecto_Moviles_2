package com.example.inventarioapp;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ajustes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ajustes extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tv_nombre, tv_pass, tv_email, tv_dir, tv_id;
    private Button btn_guardar, btn_foto, btn_huella;
    private ImageView foto;
    private String imagenSubida = "";
    private ImageButton btn_search;


    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 2;
    private String base64Image;
    private static final String PREFS_NAME = "MyPrefsFile";


    public Ajustes() {
        // Required empty public constructor
    }


    public static Ajustes newInstance(String param1, String param2) {
        Ajustes fragment = new Ajustes();
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

        View view = inflater.inflate(R.layout.fragment_ajustes, container, false);

        tv_id = view.findViewById(R.id.tv_id);
        tv_nombre = view.findViewById(R.id.tv_nombre);
        tv_pass = view.findViewById(R.id.tv_pass);
        tv_email = view.findViewById(R.id.tv_email);
        tv_dir = view.findViewById(R.id.tv_dir);

        btn_guardar = view.findViewById(R.id.btn_guardar);
        btn_foto = view.findViewById(R.id.btn_foto);
        btn_search = view.findViewById(R.id.btn_search);

        foto = view.findViewById(R.id.foto);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                if(tv_id.getText().toString().length() != 0){
                    obtenerInfo();
                }else{
                    Toast.makeText(getActivity(), "Ingrese un ID", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear el diálogo de selección de imagen
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Seleccionar imagen");
                builder.setItems(new CharSequence[]{"Cámara", "Galería"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Abrir la cámara
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                break;
                            case 1:
                                // Abrir la galería de imágenes
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, GALLERY_REQUEST);
                                break;
                        }
                    }
                });

                // Mostrar el diálogo de selección de imagen
                builder.show();
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tv_id.getText().toString().length() == 0 || tv_nombre.getText().toString().length() == 0 || tv_pass.getText().toString().length() == 0
                || tv_email.getText().toString().length() == 0 || tv_dir.getText().toString().length() == 0){

                    if(tv_id.getText().toString().length() == 0){
                        Toast.makeText(getActivity(), "Ingrese un ID", Toast.LENGTH_SHORT).show();
                    }else if(tv_nombre.getText().toString().length() == 0){
                        Toast.makeText(getActivity(), "Ingrese un Nombre", Toast.LENGTH_SHORT).show();
                    }else if(tv_pass.getText().toString().length() == 0){
                        Toast.makeText(getActivity(), "Ingrese un Contraseña", Toast.LENGTH_SHORT).show();
                    }else if(tv_email.getText().toString().length() == 0 ){
                        Toast.makeText(getActivity(), "Ingrese un Correo", Toast.LENGTH_SHORT).show();
                    }else if(tv_dir.getText().toString().length() == 0){
                        Toast.makeText(getActivity(), "Ingrese un Dirección", Toast.LENGTH_SHORT).show();
                    }


                }else{
                    actualizarPerfil();
                }

            }
        });

        return view;
    }

    public void guardarFoto() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "https://ceruminous-helmsman.000webhostapp.com/subir_imagen.php";

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String responseData = new String(response.data);
                try {
                    JSONObject jsonResponse = new JSONObject(responseData);
                    String data = jsonResponse.getString("data");
                    System.out.println((data));
                    imagenSubida = data;

                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put("imagen", "data:image/png;base64, "+base64Image); // Reemplaza "Hola Soy Imagen" con tu String
                return params;
            }
        };

        queue.add(request);
    }


    public void actualizarPerfil(){

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        String userId = settings.getString("id_usuario", "");
        System.out.println("dwdwdwdw: "+  userId);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "https://ceruminous-helmsman.000webhostapp.com/actualizar_perfil.php";

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
                        Toast.makeText(getActivity(), "Perfil de Usuario Actualizado", Toast.LENGTH_SHORT).show();

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
                params.put("id", tv_id.getText().toString());
                params.put("url_foto", imagenSubida);
                params.put("nombre", tv_nombre.getText().toString());
                params.put("psw", tv_pass.getText().toString());

                try {
                    String direccion = tv_dir.getText().toString();
                    params.put("direccion", URLEncoder.encode(direccion, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                params.put("email", tv_email.getText().toString());

                return params;
            }
        };

        queue.add(request);
    }

    public void obtenerInfo() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        String userId = settings.getString("id_usuario", "");

        String url = "https://ceruminous-helmsman.000webhostapp.com/obtener_usuario.php?id="+tv_id.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status.toString().equals("success")) {

                                String url_foto = jsonObject.getString("url_foto");
                                String nombre = jsonObject.getString("nombre");
                                String correo = jsonObject.getString("correo");
                                String contraseña = jsonObject.getString("contraseña");
                                String direccion = jsonObject.getString("direccion");

                                tv_nombre.setText(nombre);
                                tv_email.setText(correo);
                                tv_dir.setText(direccion);
                                tv_pass.setText(contraseña);

                                imagenSubida = url_foto;

                                if(!url_foto.equals("null")){
                                    String imageUrl =  "https://ceruminous-helmsman.000webhostapp.com/img/"+url_foto;
                                    Glide.with(getActivity())
                                            .load(imageUrl)
                                            .override(200, 200)
                                            .into(foto);
                                }


                            }else{
                                Toast.makeText(getActivity(), "El Usuario no Existe", Toast.LENGTH_SHORT).show();

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

    /*public void actualizarPerfil(View view){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
        String idPefil = localStorage.getString("idPerfil", null);

        String url = "http://192.168.137.23:3000/perfil/actualizar/"+idPefil;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "false"){
                                Snackbar.make(view, "Error al Actualizar Perfil Verifique sus Campos", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{
                                Snackbar.make(view, "Perfil Actualizado", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Snackbar.make(view, "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
        ){
            //Establecer encabezados
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("nameImg",nameUploaded);
                    jsonBody.put("nombre", name.getText().toString());
                    jsonBody.put("correo", correo.getText().toString());
                    jsonBody.put("contra", contra.getText().toString());
                    jsonBody.put("apellidos", apellidos.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        queue.add(request);
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && data != null && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);


            foto.setImageBitmap(photo);
            guardarFoto();
        }


        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Glide.with(this).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    foto.setImageBitmap(decodedBitmap);
                    guardarFoto();
                }
            });
        }


    }


}