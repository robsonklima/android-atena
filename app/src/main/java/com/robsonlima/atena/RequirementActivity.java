package com.robsonlima.atena;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.robsonlima.atena.api.APIClient;
import com.robsonlima.atena.interfaces.RequirementInterface;

import com.robsonlima.atena.models.Requirement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequirementActivity extends AppCompatActivity {

    private String requirementId;
    ProgressDialog progress;
    RequirementInterface requirementInterface;
    Requirement requirement;
    TextView etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}
