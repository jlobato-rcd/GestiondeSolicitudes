package com.rcdhotels.gestiondesolicitudes.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.RequestRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.main.MainActivity;
import com.rcdhotels.gestiondesolicitudes.task.GetExtRequestAsyncTask;
import com.rcdhotels.gestiondesolicitudes.task.GetRetRequestAsyncTask;
import com.rcdhotels.gestiondesolicitudes.ui.activities.RetMaterialSelectionActivity;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.currentFragment;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;

public class ReturnFragment extends Fragment {

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerViewRequests;
    private RequestRecyclerViewAdapter adapter;
    private String date;
    private int status;
    private FloatingActionButton fab2;

    @SuppressLint("SimpleDateFormat")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_return, container, false);
        currentFragment = "Return";
        setHasOptionsMenu(true);
        swipeLayout = root.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(() -> {
            date = "";
            status = -1;
            new GetRetRequestAsyncTask(getContext(), date, status).execute();
        });

        fab2 = root.findViewById(R.id.fab_2);
        fab2.setOnClickListener(v -> {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                startActivity(new Intent(getContext(), RetMaterialSelectionActivity.class));
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            } else {
                Toast.makeText(getContext(), R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
            }
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
        date = "";
        status = -1;
        new GetRetRequestAsyncTask(getContext(), date, status).execute();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NotNull MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        recyclerViewRequests = getActivity().findViewById(R.id.recyclerViewRequests);
        adapter = (RequestRecyclerViewAdapter) recyclerViewRequests.getAdapter();
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
                    adapter = (RequestRecyclerViewAdapter) recyclerViewRequests.getAdapter();
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
        adapter = (RequestRecyclerViewAdapter) recyclerViewRequests.getAdapter();

        if (item.getItemId() == R.id.action_date){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
            DatePicker picker = new DatePicker(getContext());
            picker.setCalendarViewShown(false);
            builder.setView(picker);
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                builder.create().dismiss();
            });
            builder.setPositiveButton(R.string.menu_confirm, (dialog, which) -> {
                int day = picker.getDayOfMonth();
                String dd;
                if (day < 10)
                    dd = "0" + day;
                else
                    dd = String.valueOf(day);
                int month = picker.getMonth() + 1;
                String mm;
                if (month < 10)
                    mm = "0" + month;
                else
                    mm = String.valueOf(month);
                int year = picker.getYear();
                date = year+"-"+mm+"-"+dd;
                new GetRetRequestAsyncTask(getContext(), date, which).execute();
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
