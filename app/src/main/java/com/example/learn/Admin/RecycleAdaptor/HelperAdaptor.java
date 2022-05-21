package com.example.learn.Admin.RecycleAdaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.Helper_class.Interface.SubjectItemClickListener;
import com.example.learn.R;
import com.example.learn.Helper_class.Model.Channel;

import java.util.ArrayList;

public class HelperAdaptor extends RecyclerView.Adapter implements Filterable {

    Context context;
    ArrayList<Channel> list;
    SubjectItemClickListener subjectItemClickListener;

    // To filter data
    ArrayList<Channel> fullList;

    public HelperAdaptor(Context context, ArrayList<Channel> list, SubjectItemClickListener subjectItemClickListener) {
        this.context = context;
        this.fullList = list;
        this.subjectItemClickListener = subjectItemClickListener;
        this.list = new ArrayList<>(fullList);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);

        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;

        Channel channel = list.get(position);
        viewHolderClass.subname.setText(channel.getSub_name());
        viewHolderClass.subdept.setText(channel.getCategory());


        /*viewHolderClass.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, Content.class)
                        .putExtra("subCode",channel.getSub_code())
                        .putExtra("subName",channel.getSub_name())
                        .putExtra("subDesc",channel.getSub_desc())
                        .putExtra("Instructor",channel.getSub_author()));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return sub_Filter;
    }

    private final Filter sub_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Channel> filterResult = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filterResult.addAll(fullList);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Channel ch : fullList) {
                    if (ch.getSub_name().toLowerCase().contains(filterPattern))
                        filterResult.add(ch);
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filterResult;
                //results.count = filterResult.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list.clear();
                list.addAll((ArrayList)filterResults.values);
                notifyDataSetChanged();
            }
    };

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView subname,subdept;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            subname = itemView.findViewById(R.id.subject_name_SL);
            subdept = itemView.findViewById(R.id.subject_dept_SL);

            // [CLICK EVENT]
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subjectItemClickListener.onSubjectClick(list.get(getAdapterPosition()));
                }
            });
        }
    }
}
