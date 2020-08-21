package com.rcdhotels.gestiondesolicitudes.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Material;

import java.util.ArrayList;

public class ExtReqMatRecyclerViewAdapter extends RecyclerView.Adapter<ExtReqMatRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Material> list;
    private Context mContext;

    public ExtReqMatRecyclerViewAdapter(Activity mContext, ArrayList<Material> list) {
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
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_req_upd_mat, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        Material material = list.get(position);

        viewHolder.textViewMaktx.setText(material.getMAKTX());
        viewHolder.editTextEntryUom.setText(material.getENTRY_UOM());
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
        viewHolder.itemView.setTag(viewHolder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewMaktx;
        private EditText editTextReqQnt;
        private EditText editTextEntryUom;

        public ViewHolder(View v) {
            super(v);
            textViewMaktx = v.findViewById(R.id.textViewMaktx);
            editTextReqQnt = v.findViewById(R.id.editTextReqQnt);
            editTextEntryUom = v.findViewById(R.id.editTextEntryUom);
            itemView.setTag(v);
        }
    }
}