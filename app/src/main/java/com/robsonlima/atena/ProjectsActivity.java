package com.robsonlima.atena;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    ArrayAdapter<Project> listProjectsAdapter;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.projects_activity);

        projectInterface = APIClient.getClient().create(ProjectInterface.class);
        listProjects = (ListView) findViewById(R.id.listProjects);

        onLoadProjects();

        listProjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Project project = (Project) parent.getItemAtPosition(pos);
                Intent intent = new Intent(ProjectsActivity.this, ProjectActivity.class);
                intent.putExtra("projectId", project._id);
                startActivity(intent);
            }
        });

        listProjects.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                Project project = (Project) parent.getItemAtPosition(pos);
                deleteProject(project);

                return true;
            }
        });
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
                loadListProjects();
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

    private void loadListProjects() {
        listProjectsAdapter = new ArrayAdapter<Project>(ProjectsActivity.this,
                android.R.layout.simple_list_item_1, projects);
        listProjects.setAdapter(listProjectsAdapter);
    }

    private void removeItemFromListProjects(Project project) {
        listProjectsAdapter.remove(project);
        listProjectsAdapter.notifyDataSetChanged();
    }

    private void deleteProject(final Project project) {
        new AlertDialog.Builder(this)
            .setTitle("Confirm")
            .setMessage("Do you really want to delete this project?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    progress = new ProgressDialog(ProjectsActivity.this);
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while deleting...");
                    progress.setCancelable(false);
                    progress.show();

                    Call<Project> call = projectInterface.deleteProject(project._id);
                    call.enqueue(new Callback<Project>() {
                        @Override
                        public void onResponse(Call<Project> call, Response<Project> response) {
                            Snackbar.make(findViewById(R.id.projectsActivity),project.name +
                                    " deleted!", Snackbar.LENGTH_LONG).show();

                            removeItemFromListProjects(project);

                            progress.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Project> call, Throwable t) {
                            call.cancel();

                            progress.dismiss();
                        }
                    });
                }})
            .setNegativeButton(android.R.string.no, null).show();
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
