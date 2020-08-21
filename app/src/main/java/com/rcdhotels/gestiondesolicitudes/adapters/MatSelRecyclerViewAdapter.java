package com.rcdhotels.gestiondesolicitudes.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.interfaces.ItemClickListener;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;

import java.util.ArrayList;
import java.util.List;

import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.findMaterial;

public class MatSelRecyclerViewAdapter extends RecyclerView.Adapter<MatSelRecyclerViewAdapter.ViewHolder> implements Filterable {

    private ArrayList<Material> list;
    private ArrayList<Material> arrayList;
    private Context mContext;

    public MatSelRecyclerViewAdapter(Activity mContext, ArrayList<Material> list) {
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
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_prod, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        Material material = list.get(position);

        viewHolder.textViewMaktx.setText(material.getMAKTX());
        viewHolder.textViewStgeLoc.setText(material.getSTGE_LOC());
        viewHolder.textViewMaterial.setText(String.valueOf(material.getMATERIAL()));
        viewHolder.textViewVerpr.setText("$" + material.getVERPR());
        viewHolder.editTextLabst.setText(String.valueOf(material.getLABST()));
        viewHolder.editTextEntryUom.setText(material.getENTRY_UOM());
        if (findMaterial(material.getMATERIAL()))
            viewHolder.checkBoxSelected.setChecked(true);
        viewHolder.setClickListener((view, position1, isLongClick) -> {
            if (!isLongClick) {
                if (UtilsClass.materialsToProcess == null || UtilsClass.materialsToProcess.isEmpty()){
                    UtilsClass.materialsToProcess = new ArrayList<>();
                    material.setChecked(true);
                    viewHolder.checkBoxSelected.setChecked(true);
                    UtilsClass.materialsToProcess.add(material);
                }
                else if (!findMaterial(material.getMATERIAL())){
                    if (UtilsClass.materialsToProcess.get(0).getSTGE_LOC().equalsIgnoreCase(material.getSTGE_LOC())){
                        material.setChecked(true);
                        viewHolder.checkBoxSelected.setChecked(true);
                        UtilsClass.materialsToProcess.add(material);
                    }
                    else{
                        Toast.makeText(mContext, R.string.materials_selection_message, Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    material.setChecked(false);
                    viewHolder.checkBoxSelected.setChecked(false);
                    UtilsClass.materialsToProcess.remove(material);
                }
            }
        });
    }

    @Override
    public Filter getFilter() {
        return matFilter;
    }

    private Filter matFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Material> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Material material : arrayList) {
                    if (material.getMAKTX().toLowerCase().contains(filterPattern)) {
                        filteredList.add(material);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        private CheckBox checkBoxSelected;
        private TextView textViewMaktx, textViewMaterial, textViewVerpr, textViewStgeLoc;
        private EditText editTextLabst;
        private EditText editTextEntryUom;
        private ItemClickListener clickListener;

        public ViewHolder(View v) {
            super(v);
            checkBoxSelected = v.findViewById(R.id.checkBoxSelected);
            textViewMaktx = v.findViewById(R.id.textViewMaktx);
            textViewStgeLoc = v.findViewById(R.id.textViewStgeLoc);
            textViewMaterial = v.findViewById(R.id.textViewMaterial);
            textViewVerpr = v.findViewById(R.id.textViewVerpr);
            editTextLabst = v.findViewById(R.id.editTextLabst);
            editTextEntryUom = v.findViewById(R.id.editTextEntryUom);

            itemView.setTag(v);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }
}