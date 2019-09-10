package com.gogroup.app.gogroupapp.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.HelperClasses.ApiCalls;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.DetailResponse;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import retrofit2.Response;

public class TestActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
//        GroupChannel.create("OneToOne", new String[]{ChatCamp.getCurrentUser().getId(), "6"},

        Toast.makeText(TestActivity.this, "ev ", Toast.LENGTH_LONG).show();

        GroupChannel.create("OneToOne", new String[]{UserPreferences.getInstance().getUserId(), "6"},
                true, new BaseChannel.CreateListener() {
                    @Override
                    public void onResult(BaseChannel groupChannel, ChatCampException e) {
                        Toast.makeText(TestActivity.this, "error "+e.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), ConversationActivityCamp.class);
                        intent.putExtra("channelType", "group");
                        intent.putExtra("participantState", GroupChannel.ChannelType.GROUP.name());
                        intent.putExtra("channelId", groupChannel.getId());
                        startActivity(intent);
                    }
                });



    }

}



