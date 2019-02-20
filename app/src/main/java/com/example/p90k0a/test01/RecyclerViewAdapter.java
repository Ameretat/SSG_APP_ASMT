package com.example.p90k0a.test01;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    List<MemoItem> data = new ArrayList<>(); //data
    private ItemClickListner itemlistener;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //xml 디자인한 부분 변경 (뷰홀더 생성. 한 아이템에 보여주고자 하는 아이템 레이아웃 inflate)

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.memo_row,viewGroup,false);
        //memo_row.xml 연결


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        //xml 디자인한 부분 내용 변경(데이터 뷰 바인드)
        final MemoItem currData = data.get(i);
        myViewHolder.title.setText(currData.title);
        myViewHolder.text.setText(currData.text);
        myViewHolder.date.setText(currData.date);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemlistener.onClick(currData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setItems(List<MemoItem> itemList) {
        data.clear();
        data.addAll(itemList);
    }

    public void setItemlistener(ItemClickListner itemlistener) {
        this.itemlistener = itemlistener;
    }

//    private class RowCell extends RecyclerView.ViewHolder {
//
//        public RowCell(View view) {
//            super(view);
//        }
//    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView text;
        TextView date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.dates_row);
        }
    }

    interface ItemClickListner{
        void onClick(MemoItem item);
    }
}