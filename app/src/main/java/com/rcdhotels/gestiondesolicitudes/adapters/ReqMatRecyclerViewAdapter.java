package com.rcdhotels.gestiondesolicitudes.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Material;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ReqMatRecyclerViewAdapter extends RecyclerView.Adapter<ReqMatRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Material> list;
    private Context mContext;
    private static DecimalFormat df = new DecimalFormat("0.00");
    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;
    private OnClickListener onClickListener = null;

    public ReqMatRecyclerViewAdapter(Activity mContext, ArrayList<Material> list) {
        this.list = list;
        this.mContext = mContext;
        selected_items = new SparseBooleanArray();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Material getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_req_det_mat, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        Material material = list.get(position);
        df.setRoundingMode(RoundingMode.UP);
        viewHolder.textViewMaktx.setText(material.getMAKTX());
        viewHolder.textViewMaterial.setText(String.valueOf(material.getMATERIAL()));
        viewHolder.editTextEntryUom.setText(material.getENTRY_UOM());
        viewHolder.textViewVerpr.setText(new StringBuilder().append("$").append(material.getVERPR()).toString());

        if(String.valueOf(material.getREQ_QNT()).endsWith(".0"))
            viewHolder.editTextReqQnt.setText(String.valueOf(material.getREQ_QNT()).replace(".0",""));
        else
            viewHolder.editTextReqQnt.setText(String.valueOf(material.getREQ_QNT()));

        viewHolder.textViewTotal.setText("$" + df.format(material.getREQ_QNT() * material.getVERPR()));
        viewHolder.editTextReqQnt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final EditText editTextReqQnt = viewHolder.editTextReqQnt;
                if(!editTextReqQnt.getText().toString().isEmpty()){
                    Material material1 = list.get(position);
                    if (editTextReqQnt.getText().toString().startsWith(".")) {
                        String countProd = "0" + editTextReqQnt.getText().toString();
                        material1.setREQ_QNT(Integer.parseInt(countProd));
                    }
                    else
                        material1.setREQ_QNT(Integer.parseInt(editTextReqQnt.getText().toString()));
                    viewHolder.textViewTotal.setText("$" + df.format(material1.getREQ_QNT() * material.getVERPR()));
                    list.set(position, material1);
                }
                else{
                    list.set(position, material);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        viewHolder.lyt_parent.setActivated(selected_items.get(position, false));
        viewHolder.lyt_parent.setOnClickListener(v -> {
            if (onClickListener == null) return;
            onClickListener.onItemClick(v, material, position);
        });

        viewHolder.lyt_parent.setOnLongClickListener(v -> {
            if (onClickListener == null) return false;
            onClickListener.onItemLongClick(v, material, position);
            return true;
        });
        toggleCheckedIcon(viewHolder, position);
        viewHolder.itemView.setTag(viewHolder);
    }

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

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewMaktx, textViewMaterial, textViewVerpr, textViewTotal;
        private EditText editTextReqQnt, editTextEntryUom;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            textViewMaktx = v.findViewById(R.id.textViewMaktx);
            textViewMaterial = v.findViewById(R.id.textViewMaterial);
            textViewVerpr = v.findViewById(R.id.textViewVerpr);
            textViewTotal = v.findViewById(R.id.textViewTotal);
            editTextReqQnt = v.findViewById(R.id.editTextReqQnt);
            editTextEntryUom = v.findViewById(R.id.editTextEntryUom);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            itemView.setTag(v);
        }
    }

    public interface OnClickListener {
        void onItemClick(View view, Material obj, int pos);
        void onItemLongClick(View view, Material obj, int pos);
    }
}