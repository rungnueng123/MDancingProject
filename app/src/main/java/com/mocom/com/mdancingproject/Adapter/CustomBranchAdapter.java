package com.mocom.com.mdancingproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mocom.com.mdancingproject.R;

import java.util.ArrayList;

public class CustomBranchAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> branchList;
    private LayoutInflater inflater;

    public CustomBranchAdapter(Context context, ArrayList<String> branchList, LayoutInflater inflater) {
        this.context = context;
        this.branchList = branchList;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return branchList.size();
    }

    @Override
    public Object getItem(int position) {
        return branchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_branch_item_adapter, parent, false);
            holder.branchName = convertView.findViewById(R.id.branch_name);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.branchName.setText(branchList.get(position));

        convertView.setTag(holder);

        return convertView;
    }

    public class ViewHolder {
        TextView branchName;
    }
}
