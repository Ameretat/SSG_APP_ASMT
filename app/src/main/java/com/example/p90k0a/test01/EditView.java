package com.example.p90k0a.test01;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditView extends AppCompatActivity {

    public static int numTitle = 1;
    public static String curDate = "";
    public static String curText = "";
    private EditText titleText;
    private EditText bodyText;
    private TextView dateText;
    private Long mRowId = null;

    private Cursor note;

    private DbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new DbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.edit_view);
        setTitle(R.string.app_name);

        titleText = (EditText) findViewById(R.id.title);
        bodyText = (EditText) findViewById(R.id.text);
        dateText = (TextView) findViewById(R.id.date);


        mRowId = getIntent().getLongExtra("ID",-1);
        if(mRowId != -1) {//Id가 있다(새 메모가 아닐 경우)
            titleText.setText(getIntent().getStringExtra("TITLE"));
            bodyText.setText(getIntent().getStringExtra("BODY"));
            curDate = getIntent().getStringExtra("DATE");
        }else{//--Id가 없다(새 메모일 경우)
            long msTime = System.currentTimeMillis();
            Date curDateTime = new Date(msTime);
            SimpleDateFormat formatter = new SimpleDateFormat("d'/'M'/'y");
            curDate = formatter.format(curDateTime);
        }
        /////
        dateText.setText(curDate);


        Button addBtn = findViewById(R.id.save_Btn); //--저장 버튼
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleText.getText().toString();
                String body = bodyText.getText().toString();
                String date = dateText.getText().toString();

                if(mRowId == null|| mRowId == -1){
                    long id = mDbHelper.createNote(title, body, curDate);
                    Log.d("save","id : " + id);
                    if(id > 0){
                        mRowId = id;
                    }else{
                        Log.d("saveState","failed to create note");
                    }
                }else{
                    if(!mDbHelper.updateNote(mRowId, title, body, curDate)){
                        Log.e("saveState","failed to update note");
                    }
                }
                setResult(RESULT_OK);
                finish();
            }
        });

        Button delBtn = findViewById(R.id.del_Btn); //--삭제 버튼
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(EditView.this)
                        .setTitle("Alert!")
                        .setMessage("Are you sure to delete?")
                        .setCancelable(true)
                        .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(note != null){
                                    note.close();
                                    note = null;
                                }
                                if(mRowId != null){
                                    mDbHelper.deleteNote(mRowId);
                                }
                                setResult(RESULT_OK);
                                finish();
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
        });

    }
}
