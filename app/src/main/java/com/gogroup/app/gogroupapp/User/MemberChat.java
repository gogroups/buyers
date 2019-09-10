package com.gogroup.app.gogroupapp.User;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.GroupchatAdapter;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.Models.ChatModel;
import com.gogroup.app.gogroupapp.R;

import java.util.ArrayList;

public class MemberChat extends AppCompatActivity {

    Button mButtonLeftArrow;
    LinearLayout groupInfoLayout;
    ListView messageListView;
    GroupchatAdapter groupchatAdapter;
    ArrayList<ChatModel> chatlist = new ArrayList<ChatModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_chat);
        initIds();
        setIcons();
        mButtonLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        messageListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messageListView.setStackFromBottom(true);
        getChat();

        groupchatAdapter = new GroupchatAdapter(MemberChat.this, chatlist);
        messageListView.setAdapter(groupchatAdapter);

    }
    private void initIds() {
        mButtonLeftArrow = (Button) findViewById(R.id.vendor_home_filter_left_arrow);

        messageListView = (ListView) findViewById(R.id.messagesContainer);

    }
    private void setIcons() {
        mButtonLeftArrow.setText(R.string.icon_ionicon_var_chevron_left);
        mButtonLeftArrow.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(),FontManager.IONICONFONT));
        mButtonLeftArrow.setTextSize(25);
        mButtonLeftArrow.setTextColor(ContextCompat.getColor(this, R.color.white_color));
    }

    public void getChat() {

        chatlist.add(new ChatModel("Sachin","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",0,"4:05 PM"));
        chatlist.add(new ChatModel("Sachin","Lorem Ipsum is simply dummy text of the printing and typesetting industry.",0,"5:05 PM"));
        chatlist.add(new ChatModel("Me","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",1,"5:05 PM"));

    }

}
