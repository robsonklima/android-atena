package com.robsonlima.atena;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.robsonlima.atena.api.APIClient;
import com.robsonlima.atena.interfaces.ProjectInterface;
import com.robsonlima.atena.interfaces.RequirementInterface;
import com.robsonlima.atena.models.Project;
import com.robsonlima.atena.models.Requirement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectRequirementsFragment extends Fragment {

    ProgressDialog progress;
    ProjectInterface projectInterface;
    RequirementInterface requirementInterface;
    Project project;
    ListView listRequirements;
    TextView tvMessage;
    FloatingActionButton fabCreateRequirement;
    ArrayAdapter<Requirement> listRequirementsAdapter;
    ProjectActivity projectActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_requirements_fragment, container, false);

        listRequirements = (ListView) view.findViewById(R.id.listRequirements);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        fabCreateRequirement = (FloatingActionButton) view.findViewById(R.id.fabCreateRequirement);
        requirementInterface = APIClient.getClient().create(RequirementInterface.class);
        projectInterface = APIClient.getClient().create(ProjectInterface.class);
        projectActivity = (ProjectActivity) getActivity();

        listRequirements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            Requirement requirement = (Requirement) parent.getItemAtPosition(pos);
            Intent intent = new Intent(getActivity(), RequirementActivity.class);
            intent.putExtra("requirementId", requirement._id);
            startActivity(intent);
            }
        });

        listRequirements.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
            Requirement requirement = (Requirement) parent.getItemAtPosition(pos);
            deleteRequirement(requirement);

            return true;
            }
        });

        fabCreateRequirement.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            Intent intent = new Intent(getActivity(), RequirementActivity.class);
            intent.putExtra("projectId", project._id);
            startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadProject();
    }

    private void loadProject() {
        Call<Project> call = projectInterface.getProject(projectActivity.projectId);
        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                project = (Project) response.body();

                loadListRequirements();
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                Snackbar.make(getActivity().findViewById(R.id.projectRequirementsFragment), "Error on getting data",
                        Snackbar.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void loadListRequirements() {
        if (!project.requirements.isEmpty()) {
            tvMessage.setText("");

            listRequirementsAdapter = new ArrayAdapter<Requirement>(getActivity(),
                    android.R.layout.simple_list_item_1, project.requirements);

            listRequirements.setAdapter(listRequirementsAdapter);
        } else {
            tvMessage.setText("No requirements");
        }
    }

    private void deleteRequirement(final Requirement requirement) {
        new AlertDialog.Builder(getActivity())
            .setTitle("Confirm")
            .setMessage("Do you really want to delete this requirement?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    progress = new ProgressDialog(getActivity());
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while deleting requirement...");
                    progress.setCancelable(false);
                    progress.show();

                    Call<Void> call = requirementInterface.deleteRequirement(requirement._id);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Snackbar.make(getView().findViewById(R.id.projectRequirementsFragment),
                                    "Requirement deleted successfully!", Snackbar.LENGTH_LONG).show();

                            removeItemFromListRequirements(requirement);

                            progress.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            call.cancel();

                            progress.dismiss();
                        }
                    });
                }})
            .setNegativeButton(android.R.string.no, null).show();
    }

    private void removeItemFromListRequirements(Requirement requirement) {
        listRequirementsAdapter.remove(requirement);
        listRequirementsAdapter.notifyDataSetChanged();
    }
}
