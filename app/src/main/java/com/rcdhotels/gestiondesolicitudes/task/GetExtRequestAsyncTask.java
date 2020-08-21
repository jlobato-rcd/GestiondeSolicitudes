package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.RequestRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getRequestList;

public class GetExtRequestAsyncTask extends AsyncTask<Void, Void, Void> {

    private int status;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ArrayList<Request> items = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private SwipeRefreshLayout swipeLayout;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerViewRequest;
    private RequestRecyclerViewAdapter adapter;
    private String date;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    public GetExtRequestAsyncTask(Context context, String date, int status) {
        this.context = context;
        this.date = date;
        this.status = status;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        swipeLayout = ((Activity)context).findViewById(R.id.swipe_container);
        if (swipeLayout != null){
            swipeLayout.setColorSchemeResources(R.color.colorAccent);
            swipeLayout.post(() -> swipeLayout.setRefreshing(true));
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        UtilsClass.materialsArrayList = null;
        items = getRequestList(1, date, status);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (items == null)
            items = new ArrayList<>();
        recyclerViewRequest = ((Activity) context).findViewById(R.id.recyclerViewRequests);
        recyclerViewRequest.setHasFixedSize(true);
        recyclerViewRequest.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RequestRecyclerViewAdapter((Activity) context, items);
        recyclerViewRequest.setAdapter(adapter);
        adapter.setOnClickListener(new RequestRecyclerViewAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, Request request, int pos) {
                if (adapter.getSelectedItemCount() > 0) {
                    if (user.getRole().equalsIgnoreCase("GS_AUTOR1") && request.getSTATUS() == 1 && request.getRELEASE() == 0) {
                        enableActionMode(pos);
                    }
                    else if (user.getRole().equalsIgnoreCase("GS_AUTOR2") && request.getSTATUS() == 1 && request.getRELEASE() == 1) {
                        enableActionMode(pos);
                    }
                    else if (user.getRole().equalsIgnoreCase("GS_AUTOR3") && request.getSTATUS() == 1 && request.getRELEASE() == 2) {
                        enableActionMode(pos);
                    }
                    else{
                        Toast.makeText(context, R.string.cannot_be_selected_request, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    new GetReqDetailsAsyncTask(request.getIDREQUEST(), context).execute();
                }
            }
            @Override
            public void onItemLongClick(View view, Request request, int pos) {
                if (user.getRole().equalsIgnoreCase("GS_AUTOR1") && request.getSTATUS() == 1 && request.getRELEASE() == 0) {
                    enableActionMode(pos);
                }
                else if (user.getRole().equalsIgnoreCase("GS_AUTOR2") && request.getSTATUS() == 1 && request.getRELEASE() == 1) {
                    enableActionMode(pos);
                }
                else if (user.getRole().equalsIgnoreCase("GS_AUTOR3") && request.getSTATUS() == 1 && request.getRELEASE() == 2) {
                    enableActionMode(pos);
                }
                else{
                    Toast.makeText(context, R.string.cannot_be_selected_request, Toast.LENGTH_SHORT).show();
                }
            }
        });
        actionModeCallback = new ActionModeCallback();
        recyclerViewRequest.setVisibility(View.VISIBLE);
        if (swipeLayout != null) {
            Handler handler = new Handler();
            handler.postDelayed(() -> swipeLayout.setRefreshing(false), 1000);
        }
    }

    private void enableActionMode(int position) {
        recyclerViewRequest = ((Activity)context).findViewById(R.id.recyclerViewRequests);
        adapter = (RequestRecyclerViewAdapter) recyclerViewRequest.getAdapter();
        if (actionMode == null) {
            actionMode = ((AppCompatActivity)context).startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position, adapter);
    }

    private void toggleSelection(int position, RequestRecyclerViewAdapter adapter) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(count + " " + context.getString(R.string.selected));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Tools.setSystemBarColor((Activity) context, R.color.colorPrimaryDark);
            mode.getMenuInflater().inflate(R.menu.menu_req_selected, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_confirm) {
                ArrayList<Request> requests = getSelectedItems();
                new AuthorizeRequestsAsyncTask(requests, context).execute();
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            recyclerViewRequest = ((Activity)context).findViewById(R.id.recyclerViewRequests);
            adapter = (RequestRecyclerViewAdapter) recyclerViewRequest.getAdapter();
            adapter.clearSelections();
            actionMode = null;
            Tools.setSystemBarColor((Activity) context, R.color.colorPrimaryDark);
        }
    }

    private ArrayList<Request> getSelectedItems() {
        recyclerViewRequest = ((Activity)context).findViewById(R.id.recyclerViewRequests);
        adapter = (RequestRecyclerViewAdapter) recyclerViewRequest.getAdapter();
        List<Integer> selectedItemPositions = adapter.getSelectedItems();
        ArrayList<Request> requests = new ArrayList<>();
        for (int i = 0; i < selectedItemPositions.size(); i++) {
            requests.add(adapter.getItem(selectedItemPositions.get(i)));
        }

        return requests;
    }
}
