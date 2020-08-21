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

import androidx.recyclerview.widget.RecyclerView;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Material;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class UpdExtReqMatRecyclerViewAdapter extends RecyclerView.Adapter<UpdExtReqMatRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Material> list;
    private Context mContext;
    private static DecimalFormat df = new DecimalFormat("0.00");

    public UpdExtReqMatRecyclerViewAdapter(Activity mContext, ArrayList<Material> list) {
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

        viewHolder.textViewMaktx.setText(material.getMAKTX());
        viewHolder.textViewMaterial.setText(String.valueOf(material.getMATERIAL()));
        viewHolder.editTextEntryUom.setText(material.getENTRY_UOM());
        viewHolder.textViewVerpr.setText("$" + material.getVERPR());
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
                        String countMat = "0" + editTextReqQnt.getText().toString();
                        material1.setREQ_QNT(Float.parseFloat(countMat));
                    }
                    else
                        material1.setREQ_QNT(Float.parseFloat(editTextReqQnt.getText().toString()));
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

        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (material.getDELETE() == 0){
                material.setDELETE(1);
            }
            else {
                material.setDELETE(0);
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
