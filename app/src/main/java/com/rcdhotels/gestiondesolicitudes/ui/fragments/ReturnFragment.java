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
import com.rcdhotels.gestiondesolicitudes.task.GetRetRequestAsyncTask;
import com.rcdhotels.gestiondesolicitudes.ui.dialog.RetFilterDialog;
import com.rcdhotels.gestiondesolicitudes.ui.dialog.RetFilterMaterialsDialog;

import org.jetbrains.annotations.NotNull;

import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.currentFragment;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;

public class ReturnFragment extends Fragment {

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerViewRequests;
    private ReqRecyclerViewAdapter adapter;
    private FloatingActionButton fab2;
    private int status = -1;

    @SuppressLint("SimpleDateFormat")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_return, container, false);
        currentFragment = "Return";
        setHasOptionsMenu(true);
        swipeLayout = root.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(() -> {
            if (user.getRole().equalsIgnoreCase("GS_REPRO")){
                status = 6;
            }
            if(user.getRole().equalsIgnoreCase("GS_AUTOR2") || user.getRole().equalsIgnoreCase("GS_AUTOR3") || user.getRole().equalsIgnoreCase("GS_REPRO"))
                new GetRetRequestAsyncTask(getContext(), "", "", status,  "", "", "").execute();
            else
                new GetRetRequestAsyncTask(getContext(), "", "", status,  user.getWarehouse(), "", "").execute();
        });

        fab2 = root.findViewById(R.id.fab_2);
        fab2.setOnClickListener(v -> {
            RetFilterMaterialsDialog retFilterMaterialsDialog = new RetFilterMaterialsDialog(requireContext());
            retFilterMaterialsDialog.show();
            Window window = retFilterMaterialsDialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        });
        if (user.getRole().contains("GS_SOLIC")){
            fab2.setVisibility(View.VISIBLE);
        }
        else{
            fab2.setVisibility(View.INVISIBLE);
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
            new GetRetRequestAsyncTask(getContext(), "", "", status,  "", "", "").execute();
        else
            new GetRetRequestAsyncTask(getContext(), "", "", status,  user.getWarehouse(), "", "").execute();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NotNull MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        recyclerViewRequests = getActivity().findViewById(R.id.recyclerViewRequests);
        adapter = (ReqRecyclerViewAdapter) recyclerViewRequests.getAdapter();
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
                else if (adapter == null)
                    adapter = (ReqRecyclerViewAdapter) recyclerViewRequests.getAdapter();
                else {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        recyclerViewRequests = getActivity().findViewById(R.id.recyclerViewRequests);
        adapter = (ReqRecyclerViewAdapter) recyclerViewRequests.getAdapter();

        if (item.getItemId() == R.id.action_filter){
            RetFilterDialog retFilterDialog = new RetFilterDialog(getActivity());
            retFilterDialog.show();
            Window window = retFilterDialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        return super.onOptionsItemSelected(item);
    }
}
