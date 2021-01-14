package com.rcdhotels.gestiondesolicitudes.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.ReqRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.task.GetExtRequestAsyncTask;
import com.rcdhotels.gestiondesolicitudes.ui.dialog.ExtFilterMaterialsDialog;
import com.rcdhotels.gestiondesolicitudes.ui.dialog.ExtFilterDialog;

import org.jetbrains.annotations.NotNull;

import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.currentFragment;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;

public class ExtractionFragment extends Fragment {

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerViewRequests;
    private ReqRecyclerViewAdapter mAdapter;
    private FloatingActionButton fab1;
    private int status = -1;

    @SuppressLint("SimpleDateFormat")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_extraction, container, false);
        currentFragment = "Extraction";
        setHasOptionsMenu(true);
        swipeLayout = root.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(() -> {
            if (user.getRole().equalsIgnoreCase("GS_REPRO")){
                status = 6;
            }
            if(user.getRole().equalsIgnoreCase("GS_AUTOR2") || user.getRole().equalsIgnoreCase("GS_AUTOR3") || user.getRole().equalsIgnoreCase("GS_REPRO"))
                new GetExtRequestAsyncTask(getContext(), "", "", status, "", "", "").execute();
            else
                new GetExtRequestAsyncTask(getContext(), "", "", status, "", user.getWarehouse(), "").execute();
        });
        fab1 = root.findViewById(R.id.fab_1);
        fab1.setOnClickListener(v -> {
            ExtFilterMaterialsDialog extFilterMaterialsDialog = new ExtFilterMaterialsDialog(requireContext());
            extFilterMaterialsDialog.show();
            Window window = extFilterMaterialsDialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        });

        if (user.getRole().contains("GS_SOLIC")){
            fab1.setVisibility(View.VISIBLE);
        }
        else{
            fab1.setVisibility(View.INVISIBLE);
        }
        return root;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onResume() {
        super.onResume();
        if (user.getRole().equalsIgnoreCase("GS_REPRO")){
            status = 6;
        }
        if(user.getRole().equalsIgnoreCase("GS_AUTOR2") || user.getRole().equalsIgnoreCase("GS_AUTOR3") || user.getRole().equalsIgnoreCase("GS_REPRO"))
            new GetExtRequestAsyncTask(getContext(), "", "", status, "", "", "").execute();
        else
            new GetExtRequestAsyncTask(getContext(), "", "", status, "", user.getWarehouse(), "").execute();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NotNull MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        recyclerViewRequests = getActivity().findViewById(R.id.recyclerViewRequests);
        mAdapter = (ReqRecyclerViewAdapter) recyclerViewRequests.getAdapter();
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (recyclerViewRequests == null)
                    recyclerViewRequests = getActivity().findViewById(R.id.recyclerViewRequests);
                else if (mAdapter == null)
                    mAdapter = (ReqRecyclerViewAdapter) recyclerViewRequests.getAdapter();
                else {
                    mAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        recyclerViewRequests = getActivity().findViewById(R.id.recyclerViewRequests);
        mAdapter = (ReqRecyclerViewAdapter) recyclerViewRequests.getAdapter();

        if (item.getItemId() == R.id.action_filter){
            ExtFilterDialog extFilterDialog = new ExtFilterDialog(getActivity());
            extFilterDialog.show();
            Window window = extFilterDialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        return super.onOptionsItemSelected(item);
    }
}
