package com.robsonlima.atena;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.robsonlima.atena.api.APIClient;
import com.robsonlima.atena.interfaces.ProjectInterface;
import com.robsonlima.atena.models.Project;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    ProgressDialog progress;
    ProjectInterface projectInterface;
    EditText etName;
    String projectId;
    Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_activity);

        projectInterface = APIClient.getClient().create(ProjectInterface.class);
        etName = (EditText) findViewById(R.id.etName);

        if (getIntent().hasExtra("projectId")) {
            projectId = getIntent().getStringExtra("projectId");
            onLoadProject();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ProjectEditFragment fr1 = new ProjectEditFragment();
                    return fr1;
                case 1:
                    ProjectRequirementsFragment fr2 = new ProjectRequirementsFragment();
                    return fr2;
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return R.string.title_project_edit_fragment+"";
                case 1:
                    return R.string.title_project_requirements_fragment+"";
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private void onLoadProject() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

        Call<Project> call = projectInterface.getProject(projectId);
        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                project = (Project) response.body();
                etName.setText("OLA");
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                Snackbar.make(findViewById(R.id.projectActivity), "Error on getting data",
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

        if (projectId != "") {
            Project newProject = new Project(name);
            createProject(newProject);
        } else {
            updateProject(project);
        }

        closeKeyboard(view);
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
                Project project = response.body();

                Snackbar.make(findViewById(R.id.projectActivity), project.name + " created!",
                        Snackbar.LENGTH_LONG).show();

                progress.dismiss();
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                call.cancel();

                progress.dismiss();
            }
        });
    }

    private void updateProject(final Project project) {
        progress = new ProgressDialog(this);
        progress.setTitle("Processing");
        progress.setMessage("Wait while updating project...");
        progress.setCancelable(false);
        progress.show();

        Call<Project> call = projectInterface.updateProject(project._id, project);
        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                Project project = response.body();

                Snackbar.make(findViewById(R.id.projectActivity), project.name + " updated!",
                        Snackbar.LENGTH_LONG).show();

                progress.dismiss();
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
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
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
