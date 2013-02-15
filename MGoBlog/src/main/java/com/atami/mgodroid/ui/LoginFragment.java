package com.atami.mgodroid.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.atami.mgodroid.R;
import com.atami.mgodroid.events.LoginTaskStatus;
import com.atami.mgodroid.io.LoginTask;
import com.atami.mgodroid.ui.base.BaseFragment;
import com.squareup.otto.Subscribe;
import com.squareup.tape.TaskQueue;

import javax.inject.Inject;

public class LoginFragment extends BaseFragment {

    public final static String TAG = "LoginFragment";

    @Inject
    TaskQueue<LoginTask> queue;

    public static LoginFragment newInstance() {
        LoginFragment f = new LoginFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.login, container, false);
        Button login = (Button) v.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText) v.findViewById(R.id.login_username)).getText().toString();
                String password = ((EditText) v.findViewById(R.id.login_password)).getText().toString();
                queue.add(new LoginTask(username, password, getTag()));
            }
        });
        return v;
    }

    @Subscribe
    public void onLoginTaskStatusUpdate(LoginTaskStatus status){
        if(status.tag.equals(getTag())){
            if(status.running){
                Toast.makeText(getActivity(), "running", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "stopped", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
