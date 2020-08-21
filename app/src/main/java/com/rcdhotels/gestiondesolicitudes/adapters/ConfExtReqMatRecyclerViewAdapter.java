package com.rcdhotels.gestiondesolicitudes.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ConfExtReqMatRecyclerViewAdapter extends RecyclerView.Adapter<ConfExtReqMatRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Material> list;
    private Context mContext;
    private static DecimalFormat df = new DecimalFormat("0.00");

    public ConfExtReqMatRecyclerViewAdapter(Activity mContext, ArrayList<Material> list) {
        this.list = list;
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
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_req_mat, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
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
        viewHolder.editTextReqQnt.setEnabled(false);
        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (material.getQNT_TO_CONF() != material.getREQ_QNT() && material.getQNT_TO_CONF() != 0){
                Toast.makeText(mContext, mContext.getString(R.string.comfirmation_error) + mContext.getString(R.string.confirmed_quantity)  + material.getQNT_TO_CONF(), Toast.LENGTH_SHORT).show();
                material.setSTATUS_CONF(0);
                viewHolder.checkBox.setChecked(false);
            }
            else if (material.getSTATUS_CONF() == 0){
                material.setSTATUS_CONF(1);
            }
            else {
                material.setSTATUS_CONF(0);
            }
        });
        viewHolder.itemView.setTag(viewHolder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewMaktx, textViewMaterial, textViewVerpr, textViewTotal;
        private EditText editTextReqQnt, editTextEntryUom;
        private CheckBox checkBox;

        public ViewHolder(View v) {
            super(v);
            textViewMaktx = v.findViewById(R.id.textViewMaktx);
            textViewMaterial = v.findViewById(R.id.textViewMaterial);
            textViewVerpr = v.findViewById(R.id.textViewVerpr);
            textViewTotal = v.findViewById(R.id.textViewTotal);
            editTextReqQnt = v.findViewById(R.id.editTextReqQnt);
            editTextEntryUom = v.findViewById(R.id.editTextEntryUom);
            checkBox = v.findViewById(R.id.checkBox);
            itemView.setTag(v);
        }
    }
}
