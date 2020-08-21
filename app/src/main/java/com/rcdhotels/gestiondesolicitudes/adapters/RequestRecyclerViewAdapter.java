package com.rcdhotels.gestiondesolicitudes.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.interfaces.ItemClickListener;
import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.task.DeleteRequestAsyncTask;
import com.rcdhotels.gestiondesolicitudes.task.GetReqDetailsAsyncTask;
import com.rcdhotels.gestiondesolicitudes.task.ReprocessRequestAsyncTask;
import com.rcdhotels.gestiondesolicitudes.ui.activities.ExtConfirmMaterialsActivity;
import com.rcdhotels.gestiondesolicitudes.ui.activities.ExtRequestAgainActivity;
import com.rcdhotels.gestiondesolicitudes.ui.activities.UpdateRequestActivity;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RequestRecyclerViewAdapter extends RecyclerView.Adapter<RequestRecyclerViewAdapter.ViewHolder> implements Filterable {

    private ArrayList<Request> list;
    private ArrayList<Request> arrayList;
    private Context mContext;
    private static DecimalFormat df = new DecimalFormat("0.00");
    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;
    private OnClickListener onClickListener = null;

    public RequestRecyclerViewAdapter(Activity mContext, ArrayList<Request> list) {
        this.list = list;
        this.arrayList = new ArrayList<>(list);
        this.mContext = mContext;
        selected_items = new SparseBooleanArray();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Request getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public RequestRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_req, viewGroup, false);
        return new RequestRecyclerViewAdapter.ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RequestRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        String[] array = mContext.getResources().getStringArray(R.array.status);
        Request request = list.get(position);
        df.setRoundingMode(RoundingMode.UP);
        viewHolder.textViewHeader.setText(request.getHEADER_TXT());
        viewHolder.textViewIdRequest.setText("NO. " + request.getIDREQUEST());
        viewHolder.textViewDate.setText(request.getCREATED_DATE().substring(0, 10));
        viewHolder.textViewVerpr.setText("$" + df.format(request.getTOTAL_VERPR()));
        viewHolder.textViewPlant.setText(request.getSTGE_LOC());
        viewHolder.textViewStatus.setText(array[request.getSTATUS()]);
        switch (request.getSTATUS()){
            case 1:
                viewHolder.textViewStatus.setTextColor(Color.parseColor("#607D8B"));
                viewHolder.textViewDescription.setText("");
                viewHolder.linearLayoutDescription.setVisibility(View.GONE);
                break;
            case 2:
                viewHolder.textViewStatus.setTextColor(Color.parseColor("#F44336"));
                if (!request.getTEXT().isEmpty()){
                    viewHolder.textViewDescription.setText(request.getTEXT());
                    viewHolder.linearLayoutDescription.setVisibility(View.VISIBLE);
                }
                else{
                    viewHolder.textViewDescription.setText("");
                    viewHolder.linearLayoutDescription.setVisibility(View.GONE);
                }
                break;
            case 3:
                viewHolder.textViewStatus.setTextColor(Color.parseColor("#4CAF50"));
                viewHolder.textViewDescription.setText("");
                viewHolder.linearLayoutDescription.setVisibility(View.GONE);
                break;
            case 4:
                viewHolder.textViewStatus.setTextColor(Color.parseColor("#FF9800"));
                viewHolder.textViewDescription.setText("");
                viewHolder.linearLayoutDescription.setVisibility(View.GONE);
                break;
            case 5:
                viewHolder.textViewStatus.setTextColor(Color.parseColor("#FF5722"));
                viewHolder.textViewDescription.setText("");
                viewHolder.linearLayoutDescription.setVisibility(View.GONE);
                break;
            case 6:
                viewHolder.textViewStatus.setTextColor(Color.parseColor("#03A9F4"));
                viewHolder.textViewDescription.setText("");
                viewHolder.linearLayoutDescription.setVisibility(View.GONE);
                break;
            case 7:
                viewHolder.textViewStatus.setTextColor(Color.parseColor("#000000"));
                viewHolder.textViewDescription.setText("");
                viewHolder.linearLayoutDescription.setVisibility(View.GONE);
                break;
        }

        switch (UtilsClass.user.getRole()){
            case "GS_AUTOR1":
                if (request.getSTATUS() == 1 && request.getRELEASE() == 0){
                    viewHolder.textViewHeader.setTypeface(null, Typeface.BOLD);
                    viewHolder.textViewIdRequest.setTypeface(null, Typeface.BOLD);
                }
                else {
                    viewHolder.textViewHeader.setTypeface(null, Typeface.NORMAL);
                    viewHolder.textViewIdRequest.setTypeface(null, Typeface.NORMAL);
                }
                break;
            case "GS_AUTOR2":
                if (request.getSTATUS() == 1 && request.getRELEASE() == 1 && request.getTYPE() == 1){
                    viewHolder.textViewHeader.setTypeface(null, Typeface.BOLD);
                    viewHolder.textViewIdRequest.setTypeface(null, Typeface.BOLD);
                }
                else {
                    viewHolder.textViewHeader.setTypeface(null, Typeface.NORMAL);
                    viewHolder.textViewIdRequest.setTypeface(null, Typeface.NORMAL);
                }
                break;
            case "GS_AUTOR3":
                if (request.getSTATUS() == 1 && request.getRELEASE() == 2 && request.getTYPE() == 1){
                    viewHolder.textViewHeader.setTypeface(null, Typeface.BOLD);
                    viewHolder.textViewIdRequest.setTypeface(null, Typeface.BOLD);
                }
                else {
                    viewHolder.textViewHeader.setTypeface(null, Typeface.NORMAL);
                    viewHolder.textViewIdRequest.setTypeface(null, Typeface.NORMAL);
                }
                break;
            case "GS_PROCE":
                if ((request.getRELEASE() == 3 && request.getSTATUS() == 3) || request.getSTATUS() == 5 || request.getSTATUS() == 6){
                    viewHolder.textViewHeader.setTypeface(null, Typeface.BOLD);
                    viewHolder.textViewIdRequest.setTypeface(null, Typeface.BOLD);
                }
                else {
                    viewHolder.textViewHeader.setTypeface(null, Typeface.NORMAL);
                    viewHolder.textViewIdRequest.setTypeface(null, Typeface.NORMAL);
                }
                if (request.getSTATUS() == 5 && request.getCONF() == 2){
                    viewHolder.textViewHeader.setTypeface(null, Typeface.BOLD);
                    viewHolder.textViewIdRequest.setTypeface(null, Typeface.BOLD);
                }
                else {
                    viewHolder.textViewHeader.setTypeface(null, Typeface.NORMAL);
                    viewHolder.textViewIdRequest.setTypeface(null, Typeface.NORMAL);
                }
                break;
            case "GS_SOLIC1":
            case "GS_SOLIC2":
                if (request.getSTATUS() == 1 && request.getRELEASE() > 0){
                    viewHolder.textViewReleased.setText(mContext.getString(R.string.level) + ": " + request.getRELEASE());
                    viewHolder.textViewReleased.setTextColor(Color.parseColor("#4CAF50"));
                    viewHolder.textViewReleased.setVisibility(View.VISIBLE);
                }
                if (request.getSTATUS() == 4 || request.getSTATUS() == 6){
                    viewHolder.textViewHeader.setTypeface(null, Typeface.BOLD);
                    viewHolder.textViewIdRequest.setTypeface(null, Typeface.BOLD);
                }
                break;
        }
        viewHolder.lyt_parent.setActivated(selected_items.get(position, false));
        viewHolder.lyt_parent.setOnClickListener(v -> {
            if (onClickListener == null) return;
            onClickListener.onItemClick(v, request, position);
        });

        if (UtilsClass.user.getRole().contains("GS_SOLIC")) {
            viewHolder.lyt_parent.setOnLongClickListener(v -> {
                PopupMenu popup = new PopupMenu(mContext, v);
                switch (request.getSTATUS()) {
                    case 1:
                        popup.inflate(R.menu.menu_request_created);
                        break;
                    case 2:
                        popup.inflate(R.menu.menu_request_rejected);
                        break;
                    case 3:
                    case 6:
                        popup.inflate(R.menu.menu_request_released_reprocessing);
                        break;
                    case 4:
                        popup.inflate(R.menu.menu_request_tobeconfirmed);
                        break;
                }
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.action_update) {
                        UtilsClass.currentRequest = request;
                        mContext.startActivity(new Intent(mContext, UpdateRequestActivity.class));
                    }
                    if (item.getItemId() == R.id.action_delete) {
                        request.setSTATUS(0);
                        new DeleteRequestAsyncTask(request, mContext).execute();
                    }
                    if (item.getItemId() == R.id.action_request_again) {
                        UtilsClass.currentRequest = request;
                        mContext.startActivity(new Intent(mContext, ExtRequestAgainActivity.class));
                    }
                    if (item.getItemId() == R.id.action_check_mat) {
                        UtilsClass.currentRequest = request;
                        mContext.startActivity(new Intent(mContext, ExtConfirmMaterialsActivity.class));
                    }
                    if (item.getItemId() == R.id.action_reprocess) {
                        UtilsClass.currentRequest = request;
                        new ReprocessRequestAsyncTask(mContext).execute();
                    }
                    return false;
                });
                popup.show();
                return true;
            });
        }
        else if (UtilsClass.user.getRole().contains("GS_AUTOR")) {
            viewHolder.lyt_parent.setOnLongClickListener(v -> {
                onClickListener.onItemLongClick(v, request, position);
                return true;
            });
        }
        toggleCheckedIcon(viewHolder, position);
    }

    @Override
    public Filter getFilter() {
        return equipFilter;
    }

    private Filter equipFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Request> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Request request : arrayList) {
                    if (request.getHEADER_TXT().toLowerCase().contains(filterPattern)) {
                        filteredList.add(request);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    private void toggleCheckedIcon(ViewHolder holder, int position) {
        if (selected_items.get(position, false)) {
            if (current_selected_idx == position) resetCurrentIndex();
        }
        else {
            if (current_selected_idx == position) resetCurrentIndex();
        }
    }

    public void toggleSelection(int pos) {
        current_selected_idx = pos;
        if (selected_items.get(pos, false)) {
            selected_items.delete(pos);
        } else {
            selected_items.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selected_items.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selected_items.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selected_items.size());
        for (int i = 0; i < selected_items.size(); i++) {
            items.add(selected_items.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        list.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        current_selected_idx = -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewHeader, textViewIdRequest, textViewVerpr, textViewDate, textViewStatus, textViewReleased, textViewPlant, textViewDescription;
        private View lyt_parent;
        private LinearLayout linearLayoutDescription;

        public ViewHolder(View v) {
            super(v);
            textViewHeader = v.findViewById(R.id.textViewHeader);
            textViewIdRequest = v.findViewById(R.id.textViewIdRequest);
            textViewVerpr = v.findViewById(R.id.textViewVerpr);
            textViewDate = v.findViewById(R.id.textViewDate);
            textViewStatus = v.findViewById(R.id.textViewStatus);
            textViewReleased = v.findViewById(R.id.textViewReleased);
            textViewPlant = v.findViewById(R.id.textViewPlant);
            textViewDescription = v.findViewById(R.id.textViewDescription);
            linearLayoutDescription = v.findViewById(R.id.linearLayoutDescription);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            itemView.setTag(v);
        }
    }

    public interface OnClickListener {
        void onItemClick(View view, Request obj, int pos);
        void onItemLongClick(View view, Request obj, int pos);
    }
}
