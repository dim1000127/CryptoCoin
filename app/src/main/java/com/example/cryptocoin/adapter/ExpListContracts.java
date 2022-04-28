package com.example.cryptocoin.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cryptocoin.R;
import com.example.cryptocoin.pojo.metadatapojo.ContractAddress;

import java.util.List;

public class ExpListContracts extends BaseExpandableListAdapter {

    private List<List<ContractAddress>> listGroups;

    public ExpListContracts(List<List<ContractAddress>> listGroups){
        this.listGroups = listGroups;
    }

    @Override
    public int getGroupCount() {
        return listGroups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listGroups.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return listGroups.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listGroups.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        if (view == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_exp_lv_group, viewGroup, false);
        }

        TextView textViewNameGroup = (TextView) view.findViewById(R.id.tv_name_group);
        textViewNameGroup.setText("Контракты");
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        if (view == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_exp_lv_contracts, viewGroup, false);
        }

        if (i1 == 0) {
            int paddingDp = 15;
            float density = context.getResources().getDisplayMetrics().density;
            int paddingPixel = (int)(paddingDp * density);
            LinearLayout container = (LinearLayout) view.findViewById(R.id.layout_container_contracts);
            container.setPadding(paddingPixel,0,paddingPixel,paddingPixel);
        }

        TextView textViewNamePlatform = (TextView) view.findViewById(R.id.tv_name_platform);
        TextView textViewContracts = (TextView) view.findViewById(R.id.tv_contracts);
        ImageView imageViewCopy = (ImageView) view.findViewById(R.id.btn_copy_contracts);

        textViewNamePlatform.setText(listGroups.get(i).get(i1).getPlatformCoin().getNamePlatform());
        textViewContracts.setText(listGroups.get(i).get(i1).getContractAddress());

        imageViewCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", textViewContracts.getText());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Контракт скопирован", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        if (i1 == (getChildrenCount(i)-1)){
            View viewDivider = (View) view.findViewById(R.id.view_divider);
            viewDivider.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
