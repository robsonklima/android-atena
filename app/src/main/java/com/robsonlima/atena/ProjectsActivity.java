package com.robsonlima.atena;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.robsonlima.atena.api.APIClient;
import com.robsonlima.atena.interfaces.ProjectInterface;
import com.robsonlima.atena.models.Project;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectsActivity extends AppCompatActivity {

    ProjectInterface projectInterface;
    List<Project> projects;
    ListView listProjects;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.projects_activity);

        projectInterface = APIClient.getClient().create(ProjectInterface.class);
        listProjects = (ListView) findViewById(R.id.listProjects);

        onLoadProjects();
    }

    private void onLoadProjects() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

        Call<List<Project>> call = projectInterface.getListProjects();
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                projects = response.body();
                onLoadListProjects();
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Snackbar.make(findViewById(R.id.projectsActivity), "Error on getting data",
                        Snackbar.LENGTH_LONG).show();
                call.cancel();
                progress.dismiss();
                finish();
            }
        });
    }

    private void onLoadListProjects() {
        ArrayAdapter<Project> adapter = new ArrayAdapter<Project>(ProjectsActivity.this,
                android.R.layout.simple_list_item_1, projects);
        listProjects.setAdapter(adapter);
    }

    public void onCreateProject(View view) {
        Intent intent = new Intent(ProjectsActivity.this, ProjectActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
