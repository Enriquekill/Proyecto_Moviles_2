package com.example.inventarioapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_FIRST_RUN = "firstRun";

    InventoryFragment inventario = new InventoryFragment();
    AddInventoryFragment agregarInventario = new AddInventoryFragment();
    RemoveInventoryFragment quitarInventario = new RemoveInventoryFragment();
    AddProductFragment nuevoProducto = new AddProductFragment();
    AddUserFragment nuevoUsuario = new AddUserFragment();


    //EO
    private String base64Image;
    private ImageView foto;

    private static final String BASE = "data:image/png;base64,";
    private String sImage = "";
    private String rol = "";

    private static final int CAMERA_REQUEST = 1888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        Drawer drawerManager = new Drawer();
        drawerManager.setupDrawer(this, drawerLayout);

        TopBar topBar = new TopBar();
        topBar.setupTopBar(this);
        topBar.setNavigationIcon(
                ContextCompat.getDrawable(this, R.drawable.baseline_menu_24),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else {
                            drawerLayout.openDrawer(GravityCompat.START);
                        }
                    }
                }
        );


        NavigationView navigationView = findViewById(R.id.navigationView);

        Menu navMenu = navigationView.getMenu();

        obtenerInfo(navMenu);



        View headerView = navigationView.getHeaderView(0);
        foto = headerView.findViewById(R.id.foto);
        LottieAnimationView btn_edit = headerView.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Dqdwdwdwdwdwdwdwd");
                galleryIntent();
            }
        });

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(inventario);

    }

    //EO

    private void galleryIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            foto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            foto.setImageBitmap(photo);


            guardarFoto();

        }


        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
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

                    foto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    foto.setImageBitmap(decodedBitmap);
                    guardarFoto();

                }
            });


        }


    }


    public void guardarFoto() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://ceruminous-helmsman.000webhostapp.com/subir_imagen.php";

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String responseData = new String(response.data);
                try {
                    JSONObject jsonResponse = new JSONObject(responseData);
                    String data = jsonResponse.getString("data");
                    System.out.println((data));
                    actualizarPerfil(data);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Error Intentelo más tarde", Toast.LENGTH_SHORT).show();
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


    public void actualizarPerfil(String url_foto){

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String userId = settings.getString("id_usuario", "");

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://ceruminous-helmsman.000webhostapp.com/actualizar_usuario.php";

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String message = new String(response.data);
                System.out.println(message);

                Toast.makeText(MainActivity.this, "Perfil de Usuario Actualizado", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Error Intentelo más tarde", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", userId);
                params.put("url_foto", url_foto);
                return params;
            }
        };

        queue.add(request);
    }

    public void obtenerInfo(Menu navMenu) {
        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String userId = settings.getString("id_usuario", "");

        String url = "https://ceruminous-helmsman.000webhostapp.com/obtener_usuario.php?id="+userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status.toString().equals("success")) {

                                String _rol = jsonObject.getString("rol");
                                rol = _rol;

                                MenuItem configuracionItem = navMenu.findItem(R.id.settings_item);
                                MenuItem registroUsuarios = navMenu.findItem(R.id.logout1);

                                if(rol.equals("administrador")){
                                    System.out.println("Dwdwdwdwdwdwdwdwdwdwd");
                                    configuracionItem.setVisible(true);
                                    registroUsuarios.setVisible(true);
                                }else{
                                    configuracionItem.setVisible(false);
                                    registroUsuarios.setVisible(false);
                                }

                                String url_foto = jsonObject.getString("url_foto");
                                System.out.println(url_foto);
                                String imageUrl =  "https://ceruminous-helmsman.000webhostapp.com/img/"+url_foto;
                                Glide.with(getApplicationContext())
                                        .load(imageUrl)
                                        .override(200, 200)
                                        .into(foto);

                            }else{
                                Snackbar.make(getWindow().getDecorView(), "Error al Obtener Tus Datos", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                            e.printStackTrace();
                            Snackbar.make(getWindow().getDecorView(), "Se produjo tus Datos", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getWindow().getDecorView(), "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        queue.add(stringRequest);
    }


    // Método para obtener los datos de un Bitmap en formato byte[]
    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }



    //EO/

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.inventario:
                    loadFragment(inventario);
                    return true;
                case R.id.quitar:
                    loadFragment(quitarInventario);
                    return true;
                case R.id.nuevoProducto:
                    loadFragment(nuevoProducto);
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    public void logout() {
        // Código para cerrar sesión
    }

    public void doThis(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(KEY_FIRST_RUN, true);
        editor.apply();
    }

    public void doThis1(MenuItem item) {
        loadFragment(new RegistrarUsuarios());
    }

    public void openHerramienta(MenuItem item) {
        // Código para abrir el fragmento de la Calculadora
        Intent intent = new Intent(MainActivity.this, Herramientas.class);
        startActivity(intent);

    }
    public void openAjuste(MenuItem item) {
        loadFragment(new Ajustes());

    }
}

