package com.example.p90k0a.test01;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListner {

    private DbAdapter mDbHelper;
    private RecyclerViewAdapter recyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int cnt=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new DbAdapter (this);
        mDbHelper.open(); //DB를 열고 그 주소를 가져온다. DB가 없을 경우 onCreate 호출

       //목표 리사이클러 뷰 받기
        RecyclerView view = findViewById(R.id.main_recyclerview);
        TextView tview = findViewById(R.id.memo_cnt);

        view.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerViewAdapter.setItemlistener(this);
        view.setAdapter(recyclerViewAdapter); //step4. 어댑터 연결


        // TODO
        // step1. adapter 만들어서 => XxxAdapter extends ArrayAdapter(or ...)
        // step2. data(ArrayList) 를 만들고 => List<Item> datas = new ArrayList<>(); datas.add(item); ....
        // step3. adapter 에 data를 넣어주고 => adapter.setItems(data)
        // step4. RecyclerView랑 adapter랑 연결. => RecyclerView.setAdapter(adapter)


        recyclerViewAdapter.setItems(getDBData()); //--step3 (준비한 DB로부터 뷰로 내용 받기)
        cnt = recyclerViewAdapter.getItemCount();
        tview.setText("현재 총 " + cnt  +"개의 메모가 저장돼 있습니다.");
        recyclerViewAdapter.notifyDataSetChanged();//--데이터 변경 됐을 경우 리사이클러 뷰에 고지

        //------------------------------------

        Button addBtn = findViewById(R.id.edit_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNote();
            }
        });

        String[] from = new String[] { DbAdapter.KEY_TITLE ,DbAdapter.KEY_DATE};
        int[] to = new int[] { R.id.title,R.id.dates_row};

    }

    private List<MemoItem> getDBData(){
        List<MemoItem> itemList = new ArrayList<>(); //--step2

        Cursor notesCursor = mDbHelper.fetchAllNotes(); //쿼리 결과물 가리키는 커서 전달
        //startManagingCursor(notesCursor); //액티비티가 커서를 매니징 할 수 있도록 하는 작업

        while(notesCursor.moveToNext()){
            long id;
            String title;
            String date;
            String text;

            id = notesCursor.getLong(notesCursor.getColumnIndexOrThrow(DbAdapter.KEY_ROWID));
            title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DbAdapter.KEY_TITLE));
            text = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DbAdapter.KEY_BODY));
            date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DbAdapter.KEY_DATE));

            MemoItem memoItem = new MemoItem(id,title,text,date);
            itemList.add(memoItem);
        }//--리스트에 memoItem 차례로 삽입

        return itemList;
    }

    private List<MemoItem> getDummyData() {//더미데이터 확인용
        List<MemoItem> itemList = new ArrayList<>(); //--step2

        for (int i = 0; i < 10; i++) {
            itemList.add((new MemoItem(i, "타이틀"+i, "내용"+i, "날짜"+i)));
        }

        return itemList;
    }

    private void createNote() {//--새 메모 추가 경로로 EditView 접근
        Intent i = new Intent(this, EditView.class);
//        i.putExtra("ID",-1);
//        i.putExtra("TITLE","!#@$");
        startActivityForResult(i, 1);
    }

    private void showMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//-- 후속 액티비티 결과값 반환
        super.onActivityResult(requestCode, resultCode, data);
        TextView tview = findViewById(R.id.memo_cnt);

        recyclerViewAdapter.setItems(getDBData()); //--step3
        tview.setText("현재 총 " + recyclerViewAdapter.getItemCount()  +"개의 메모가 저장돼 있습니다.");
        recyclerViewAdapter.notifyDataSetChanged(); //--데이터 변경 됐을 경우 리사이클러 뷰에 고지
    }

    @Override
    public void onClick(MemoItem item) {//클릭 시 i와 함께 EditView 액티비티 호출

        Intent i = new Intent(this, EditView.class);
        i.putExtra("ID",item.ID);
        i.putExtra("TITLE",item.title);
        i.putExtra("BODY",item.text);
        i.putExtra("DATE",item.date);

        startActivityForResult(i, 1);
    }

    @Override
    public void onLongClick(final MemoItem item) {

        //Long itemID = item.ID;
        //showMessage("LongClick Recieved");


        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Alert!")
                .setMessage("Are you sure to delete?")
                .setCancelable(true)
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDbHelper.deleteNote(item.ID);
                        recyclerViewAdapter.setItems(getDBData());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();

    }

}