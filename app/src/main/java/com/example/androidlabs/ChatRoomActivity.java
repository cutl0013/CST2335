package com.example.androidlabs;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {
    ListView lv;
    EditText editMessage;
    List<MessageModel> messageModelList2 = new ArrayList<>();
    Button btnSend;
    Button btnReceive;
    DBAdapter db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_activity);

        lv = (ListView)findViewById(R.id.listView);
        editMessage = (EditText)findViewById(R.id.editChatMessage);
        btnSend = (Button)findViewById(R.id.btnSend);
        btnReceive = (Button)findViewById(R.id.btnRecieve);
        db = new DBAdapter(this);
        viewMessage();


        btnSend.setOnClickListener(c -> {
            String message = editMessage.getText().toString();

            // MessageModel model = new MessageModel(message, true);
            // messageModelList2.add(model);
            db.insertMessage(message, true);
            editMessage.setText("");
            // ChatAdapter adt = new ChatAdapter(messageModelList2, getApplicationContext());
            // lv.setAdapter(adt);
            messageModelList2.clear();
            viewMessage();

        });

        btnReceive.setOnClickListener(c -> {
            String message = editMessage.getText().toString();
            // MessageModel model = new MessageModel(message, false);
            //  messageModelList2.add(model);
            editMessage.setText("");
            // ChatAdapter adt = new ChatAdapter(messageModelList2, getApplicationContext());
            // lv.setAdapter(adt);
            db.insertMessage(message, false);
            editMessage.setText("");
            messageModelList2.clear();
            viewMessage();
        });

        Log.e("ChatRoomActivity","onCreate");

    }
    private void viewMessage(){
        Cursor cursor = db.getAllMessages();
        //hien so message trong db
        Log.d("message",String.valueOf(cursor.getCount()));

        if (cursor.getCount() != 0){
            while (cursor.moveToNext()){
                MessageModel model = new MessageModel(cursor.getString(1), cursor.getInt(2)==0?true:false);
                messageModelList2.add(model);
                ChatAdapter adt = new ChatAdapter(messageModelList2, getApplicationContext());
                lv.setAdapter(adt);

            }//end of while
        }//end of if
    }// end of viewMessage



}//end of chatRoomActivity Class

class MessageModel {
    public String message;
    public boolean isSend;

    public MessageModel(String message, boolean isSend) {
        this.message = message;
        this.isSend = isSend;
    }

    public MessageModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}
//test folk
class ChatAdapter extends BaseAdapter {
    private List<MessageModel> messageModelList;
    private Context context;
    private LayoutInflater inflater;

    public ChatAdapter(List<MessageModel> messageModels, Context context) {
        messageModelList = messageModels;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return messageModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null){
            if (messageModelList.get(position).isSend()){
                view = inflater.inflate(R.layout.send_lab4, null);

            }else {
                view = inflater.inflate(R.layout.receive_lab4, null);
            }
            TextView  messageText = (TextView)view.findViewById(R.id.msgText);
            messageText.setText(messageModelList.get(position).message);
        }
        return view;
    }
}
