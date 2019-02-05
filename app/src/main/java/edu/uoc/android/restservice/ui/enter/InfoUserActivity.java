package edu.uoc.android.restservice.ui.enter;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import edu.uoc.android.restservice.R;
import edu.uoc.android.restservice.rest.adapter.GitHubAdapter;
import edu.uoc.android.restservice.rest.model.Followers;
import edu.uoc.android.restservice.rest.model.Owner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoUserActivity extends AppCompatActivity {

    ArrayList<Owner> listaFollowers;
    RecyclerView recyclerViewFollowers;

    TextView textViewRepositories, textViewFollowing;
    ImageView imageViewProfile;

    ProgressDialog progressDialog, progressDialog2;  // definimos el cuadro de diálogo de progreso


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        textViewFollowing = findViewById(R.id.textViewFollowing);
        textViewRepositories = findViewById(R.id.textViewRepositories);
        imageViewProfile = (ImageView) findViewById(R.id.imageViewProfile);


        listaFollowers = new ArrayList<>();
        recyclerViewFollowers = (RecyclerView)findViewById(R.id.recyclerViewFollowers);

        recyclerViewFollowers.setLayoutManager(new LinearLayoutManager(this));

        String loginName = getIntent().getStringExtra("loginName");

        //initProgressBar();

        mostrarDatosBasicos(loginName);
    }

    TextView labelFollowing, labelRepositories, labelFollowers;
    private void initProgressBar()
    {
        textViewFollowing.setVisibility(View.INVISIBLE);
        textViewRepositories.setVisibility(View.INVISIBLE);
        imageViewProfile.setVisibility(View.INVISIBLE);
        recyclerViewFollowers.setVisibility(View.INVISIBLE);

        labelFollowing = (TextView)findViewById(R.id.labelFollowing);
        labelFollowing.setVisibility(View.INVISIBLE);

        labelRepositories = (TextView) findViewById(R.id.labelRepositories);
        labelRepositories.setVisibility(View.INVISIBLE);

        labelFollowers = (TextView) findViewById(R.id.labelFollowers);
        labelFollowers.setVisibility(View.INVISIBLE);

    }

    private void mostrarDatosBasicos(String loginName){
        progressDialog = new ProgressDialog(this);  // instanciamos al progressdialog
        progressDialog.setMessage("Buscando usuario");  // asignamos un mensaje cuando se quiera buscar el usuario
        progressDialog.show();  // validamos dicho mensaje
        GitHubAdapter adapter = new GitHubAdapter();
        Call<Owner> call = adapter.getOwner(loginName);
        call.enqueue(new Callback<Owner>() {

            @Override
            public void onResponse(Call<Owner> call, Response<Owner> response) {
                progressDialog.dismiss(); // Finalización del progressdialog
                Owner owner = response.body();
                if (owner != null) {    // if para comprobar si hay resultados
                    textViewRepositories.setText(owner.getPublicRepos().toString()); // Se asigna el número de repositorios para que se muestre
                    textViewFollowing.setText(owner.getFollowing().toString()); // igualmente se asigna la cantidad de seguidores en el texto
                    Picasso.get().load(owner.getAvatarUrl()).into(imageViewProfile); // Se carga la imagen del usuario
                }else { // en caso de no haber resultados muestra el mensaje de error
                    Toast.makeText(InfoUserActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show(); // el mensaje y se valida con el .show
                }
            }

            @Override
            public void onFailure(Call<Owner> call, Throwable t) {
                // Mensaje de error en el fallo de la petición
                Toast.makeText(InfoUserActivity.this, "Ha ocurrido un error al realizar la petición", Toast.LENGTH_SHORT).show();
            }
        });

        // Mensaje de dialogo para la búsqueda de seguidores
        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("Buscando seguidores");
        progressDialog2.show(); // Se valida/activa

        // Se llama la lista de seguidores del usuario teniendo en cuenta el nombre que se usó para dicha busqueda
        Call<List<Followers>> callFollowers = new GitHubAdapter().getOwnerFollowers(loginName);

        callFollowers.enqueue(new Callback<List<Followers>>() {
            @Override
            public void onResponse(Call<List<Followers>> call, Response<List<Followers>> response) {
                progressDialog2.cancel();
                List<Followers> list = response.body(); // Se guarda el resultado en la lista followers
                if (list != null) { //if para saber si hay contenido
                    // de ser así se cargan en el adaptador para mostrarlos en el recicler view
                    AdaptadorFollowers adapter = new AdaptadorFollowers(list);
                    recyclerViewFollowers.setAdapter(adapter);
                } else {
                    // Caso contrario mostrará un mensaje de error
                    Toast.makeText(InfoUserActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Followers>> call, Throwable t) {
                // Mensaje de error para la petición
                Toast.makeText(InfoUserActivity.this, "Ha ocurrido un error al realizar la petición", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
