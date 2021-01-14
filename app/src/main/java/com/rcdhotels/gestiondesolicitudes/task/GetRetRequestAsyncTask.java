package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.ReqRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.utils.Tools;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.requestArrayList;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getRequestList;

public class GetRetRequestAsyncTask extends AsyncTask<Void, Void, Void> {

    private String dateTo;
    private String movestloc;
    private String stgeloc;
    private int status;
    private String dateFrom;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ArrayList<Request> items = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private SwipeRefreshLayout swipeLayout;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerViewRequest;
    private ReqRecyclerViewAdapter adapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private String matType;
    boolean isLoading = false;

    public GetRetRequestAsyncTask(Context context, String dateFrom, String dateTo, int status, String stgeloc, String movestloc, String matType) {
        this.context = context;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.status = status;
        this.stgeloc = stgeloc;
        this.movestloc = movestloc;
        this.matType = matType;
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
        items = getRequestList(0, dateFrom, dateTo, status, stgeloc, movestloc, matType);
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (items == null)
            items = new ArrayList<>();

        int count = items.size();
        float amount = 0;
        for (Request request : items) {
            amount += request.getTOTAL_VERPR();
        }
        TextView textViewReqCount = ((Activity)context).findViewById(R.id.textViewReqCount);
        textViewReqCount.setText(context.getString(R.string.total) + count);

        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);

        TextView textViewReqTotalVerprAmount = ((Activity)context).findViewById(R.id.textViewReqTotalVerprAmount);
        textViewReqTotalVerprAmount.setText(context.getString(R.string.amount) + "$" + df.format(amount));

        int i = 0;
        requestArrayList = new ArrayList<>();
        while (i < 10 && i < items.size()) {
            requestArrayList.add(items.get(i));
            i++;
        }

        recyclerViewRequest = ((Activity) context).findViewById(R.id.recyclerViewRequests);
        recyclerViewRequest.setHasFixedSize(true);
        recyclerViewRequest.setLayoutManager(new LinearLayoutManager(context));
        ReqRecyclerViewAdapter adapter = new ReqRecyclerViewAdapter((Activity) context, items);
        recyclerViewRequest.setAdapter(adapter);
        adapter.setOnClickListener(new ReqRecyclerViewAdapter.OnClickListener() {
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
                    else if (user.getRole().equalsIgnoreCase("GS_PROCE") && (request.getSTATUS() == 3 && request.getRELEASE() == 3) || request.getSTATUS() == 5 || request.getSTATUS() == 6) {
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
                else if (user.getRole().equalsIgnoreCase("GS_PROCE") && (request.getSTATUS() == 3 && request.getRELEASE() == 3) || request.getSTATUS() == 5 || request.getSTATUS() == 6) {
                    enableActionMode(pos);
                }
                else{
                    Toast.makeText(context, R.string.cannot_be_selected_request, Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerViewRequest.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == requestArrayList.size() - 1 && requestArrayList.size() < items.size()) {
                        //bottom of list!
                        requestArrayList.add(null);
                        adapter.notifyItemInserted(requestArrayList.size() - 1);

                        recyclerView.post(new Runnable() {
                            public void run() {
                                requestArrayList.remove(requestArrayList.size() - 1);
                                int scrollPosition = requestArrayList.size();
                                adapter.notifyItemRemoved(scrollPosition);
                                int currentSize = scrollPosition;
                                int nextLimit = currentSize + 10;

                                while (currentSize - 1 < nextLimit && currentSize < items.size()) {
                                    requestArrayList.add(items.get(currentSize));
                                    currentSize++;
                                }

                                adapter.notifyDataSetChanged();
                                isLoading = false;
                            }
                        });
                        isLoading = true;
                    }
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
        adapter = (ReqRecyclerViewAdapter) recyclerViewRequest.getAdapter();
        if (actionMode == null) {
            actionMode = ((AppCompatActivity)context).startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position, adapter);
    }

    private void toggleSelection(int position, ReqRecyclerViewAdapter adapter) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        }
        else {
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
                switch (user.getRole()){
                    case "GS_AUTOR1":
                    case "GS_AUTOR2":
                    case "GS_AUTOR3":
                        new AuthorizeRequestsListAsyncTask(requests, context).execute();
                        break;
                    case "GS_PROCE":
                        new ProcessRequestsListAsyncTask(requests, context).execute();
                        break;
                    case "GS_REPRO":
                        new ReprocessRequestsListAsyncTask(requests, context).execute();
                        break;
                }
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            recyclerViewRequest = ((Activity)context).findViewById(R.id.recyclerViewRequests);
            adapter = (ReqRecyclerViewAdapter) recyclerViewRequest.getAdapter();
            adapter.clearSelections();
            actionMode = null;
            Tools.setSystemBarColor((Activity) context, R.color.colorPrimaryDark);
        }
    }

    private ArrayList<Request> getSelectedItems() {
        recyclerViewRequest = ((Activity)context).findViewById(R.id.recyclerViewRequests);
        adapter = (ReqRecyclerViewAdapter) recyclerViewRequest.getAdapter();
        List<Integer> selectedItemPositions = adapter.getSelectedItems();
        ArrayList<Request> requests = new ArrayList<>();
        for (int i = 0; i < selectedItemPositions.size(); i++) {
            requests.add(adapter.getItem(selectedItemPositions.get(i)));
        }
        return requests;
    }
}
