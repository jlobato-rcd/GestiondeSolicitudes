package com.rcdhotels.gestiondesolicitudes.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Material;

import java.util.ArrayList;

public class ExtReqAgainMatRecyclerViewAdapter extends RecyclerView.Adapter<ExtReqAgainMatRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Material> list;
    private ArrayList<Material> arrayList;
    private Context mContext;

    public ExtReqAgainMatRecyclerViewAdapter(Activity mContext, ArrayList<Material> list) {
        this.list = list;
        this.arrayList = new ArrayList<>(list);
        this.mContext = mContext;
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
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_material, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        Material material = list.get(position);

        viewHolder.textViewMaktx.setText(material.getMAKTX());
        viewHolder.textViewMaterial.setText(String.valueOf(material.getMATERIAL()));
        viewHolder.editTextEntryUom.setText(material.getENTRY_UOM());
        viewHolder.editTextEntryUom1.setText(material.getENTRY_UOM());
        viewHolder.textViewVerpr.setText(new StringBuilder().append("$").append(material.getVERPR()).toString());
        if(String.valueOf(material.getREQ_QNT()).endsWith(".0"))
            viewHolder.editTextReqQnt.setText(String.valueOf(material.getREQ_QNT()).replace(".0",""));
        else
            viewHolder.editTextReqQnt.setText(String.valueOf(material.getREQ_QNT()));
        if(String.valueOf(material.getENTRY_QNT()).endsWith(".0"))
            viewHolder.editTextQntToConf.setText(String.valueOf(material.getENTRY_QNT()).replace(".0",""));
        else
            viewHolder.editTextQntToConf.setText(String.valueOf(material.getENTRY_QNT()));
        viewHolder.itemView.setTag(viewHolder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewMaktx, textViewMaterial, textViewVerpr;
        private EditText editTextReqQnt, editTextQntToConf, editTextEntryUom, editTextEntryUom1;

        public ViewHolder(View v) {
            super(v);
            textViewMaktx = v.findViewById(R.id.textViewMaktx);
            textViewMaterial = v.findViewById(R.id.textViewMaterial);
            textViewVerpr = v.findViewById(R.id.textViewVerpr);
            editTextReqQnt = v.findViewById(R.id.editTextReqQnt);
            editTextQntToConf = v.findViewById(R.id.editTextQntToConf);
            editTextEntryUom = v.findViewById(R.id.editTextEntryUom);
            editTextEntryUom1 = v.findViewById(R.id.editTextEntryUom1);
            itemView.setTag(v);
        }
    }
}