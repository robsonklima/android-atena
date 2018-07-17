package com.robsonlima.atena;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.robsonlima.atena.api.APIClient;
import com.robsonlima.atena.interfaces.RequirementInterface;

import com.robsonlima.atena.models.Project;
import com.robsonlima.atena.models.Requirement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequirementActivity extends AppCompatActivity {

    private String projectId;
    private String requirementId;
    ProgressDialog progress;
    RequirementInterface requirementInterface;
    Requirement requirement;
    TextView etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.requirement_activity);

        requirementInterface = APIClient.getClient().create(RequirementInterface.class);
        etName = (TextView) findViewById(R.id.etName);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().hasExtra("requirementId")) {
            requirementId = getIntent().getStringExtra("requirementId");
            loadRequirement();
        }

        if (getIntent().hasExtra("projectId")) {
            projectId = getIntent().getStringExtra("projectId");
        }
    }

    private void loadRequirement() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading requirement...");
        progress.setCancelable(false);
        progress.show();

        Call<Requirement> call = requirementInterface.getRequirement(requirementId);
        call.enqueue(new Callback<Requirement>() {
            @Override
            public void onResponse(Call<Requirement> call, Response<Requirement> response) {
                requirement = (Requirement) response.body();
                etName.setText(requirement.name);
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<Requirement> call, Throwable t) {
                Snackbar.make(findViewById(R.id.requirementActivity), "Error on getting data",
                        Snackbar.LENGTH_LONG).show();
                call.cancel();
                progress.dismiss();
                finish();
            }
        });
    }

    public void onClickSubmit(View view) {
        String name = etName.getText().toString();

        if (name.isEmpty() || name.length() < 4) {
            etName.setError("at least 4 alphanumeric characters");
            return;
        }

        if (TextUtils.isEmpty(requirementId)) {
            Requirement newRequirement = new Requirement(name);
            createRequirement(newRequirement);
        } else {
            requirement.name = name;
            updateRequirement(requirement);
        }

        closeKeyboard(view);
    }

    private void createRequirement(Requirement requirement) {
        progress = new ProgressDialog(this);
        progress.setTitle("Processing");
        progress.setMessage("Wait while creating project...");
        progress.setCancelable(false);
        progress.show();

        Call<Requirement> call = requirementInterface.createRequirement(projectId, requirement);
        call.enqueue(new Callback<Requirement>() {
            @Override
            public void onResponse(Call<Requirement> call, Response<Requirement> response) {
                Requirement requirement = response.body();

                Toast.makeText(RequirementActivity.this, requirement.name + " created!",
                        Toast.LENGTH_SHORT).show();

                progress.dismiss();
                finish();
            }

            @Override
            public void onFailure(Call<Requirement> call, Throwable t) {
                call.cancel();

                progress.dismiss();
            }
        });
    }

    private void updateRequirement(final Requirement requirement) {
        progress = new ProgressDialog(this);
        progress.setTitle("Processing");
        progress.setMessage("Wait while updating project...");
        progress.setCancelable(false);
        progress.show();

        Call<Requirement> call = requirementInterface.updateRequirement(requirement._id, requirement);
        call.enqueue(new Callback<Requirement>() {
            @Override
            public void onResponse(Call<Requirement> call, Response<Requirement> response) {
                Requirement requirement = response.body();

                Toast.makeText(RequirementActivity.this, requirement.name + " updated!",
                        Toast.LENGTH_SHORT).show();

                progress.dismiss();
                finish();
            }

            @Override
            public void onFailure(Call<Requirement> call, Throwable t) {
                call.cancel();

                progress.dismiss();
            }
        });
    }

    private void closeKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
