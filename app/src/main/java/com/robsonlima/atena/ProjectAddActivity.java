package com.robsonlima.atena;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.robsonlima.atena.api.APIClient;
import com.robsonlima.atena.interfaces.ProjectInterface;
import com.robsonlima.atena.interfaces.RequirementInterface;
import com.robsonlima.atena.models.Project;
import com.robsonlima.atena.models.Requirement;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectAddActivity extends AppCompatActivity {

    ProgressDialog progress;
    ProjectInterface projectInterface;
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.project_add_activity);

        projectInterface = APIClient.getClient().create(ProjectInterface.class);
        etName = (EditText) findViewById(R.id.etName);
    }

    public void onClickSubmit(View view) {
        String name = etName.getText().toString();

        if (name.isEmpty() || name.length() < 4) {
            etName.setError("at least 4 alphanumeric characters");
            return;
        }

        Project newProject = new Project(name);
        createProject(newProject);
    }

    private void createProject(Project project) {
        progress = new ProgressDialog(this);
        progress.setTitle("Processing");
        progress.setMessage("Wait while creating project...");
        progress.setCancelable(false);
        progress.show();

        Call<Project> call = projectInterface.createProject(project);
        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                Project resProject = response.body();

                Toast.makeText(ProjectAddActivity.this, resProject.name + " created!",
                        Toast.LENGTH_SHORT).show();

                progress.dismiss();
                finish();
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                call.cancel();

                progress.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
