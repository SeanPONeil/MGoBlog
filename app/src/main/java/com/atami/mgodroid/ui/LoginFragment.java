package com.atami.mgodroid.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.atami.mgodroid.R;
import com.atami.mgodroid.events.LoginTaskStatus;
import com.atami.mgodroid.io.LoginTask;
import com.atami.mgodroid.ui.base.BaseDialogFragment;
import com.squareup.otto.Subscribe;
import com.squareup.tape.TaskQueue;

import javax.inject.Inject;

public class LoginFragment extends BaseDialogFragment {

    public final static String TAG = "LoginFragment";

    @Inject
    TaskQueue<LoginTask> queue;

    public static LoginFragment newInstance() {
        LoginFragment f = new LoginFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        final View v = inflater.inflate(R.layout.login, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Button login = (Button) getView().findViewById(R.id.login);
        SharedPreferences prefs = getActivity().getSharedPreferences(getActivity().getPackageName(),
                Context.MODE_PRIVATE);
        String username = prefs.getString("username", "");
        if (TextUtils.isEmpty(username)) {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username = ((EditText) getView().findViewById(R.id.login_username)).getText().toString();
                    String password = ((EditText) getView().findViewById(R.id.login_password)).getText().toString();
                    queue.add(new LoginTask(username, password, getTag()));
                }
            });
        } else {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences prefs = getActivity().getSharedPreferences(getActivity().getPackageName(),
                            Context.MODE_PRIVATE);
                    prefs.edit().putString("username", "").putString("password", "").commit();

                    ((EditText) getView().findViewById(R.id.login_username)).setText("");
                    ((EditText) getView().findViewById(R.id.login_password)).setVisibility(View.VISIBLE);
                    login.setText("Login");

                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String username = ((EditText) getView().findViewById(R.id.login_username)).getText().toString();
                            String password = ((EditText) getView().findViewById(R.id.login_password)).getText().toString();
                            queue.add(new LoginTask(username, password, getTag()));
                        }
                    });
                }
            });
            ((EditText) getView().findViewById(R.id.login_username)).setText(username);
            ((EditText) getView().findViewById(R.id.login_password)).setVisibility(View.GONE);
            login.setText("Logout");
        }
    }

    @Subscribe
    public void onLoginTaskStatusUpdate(LoginTaskStatus status) {
        if (status.tag.equals(getTag())) {

            final Button login = (Button) getView().findViewById(R.id.login);

            if (status.running) {
                login.setText("Logging in...");
            }

            if (status.success) {
                String username = ((EditText) getView().findViewById(R.id.login_username)).getText().toString();
                String password = ((EditText) getView().findViewById(R.id.login_password)).getText().toString();
                SharedPreferences prefs = getActivity().getSharedPreferences(getActivity().getPackageName(),
                        Context.MODE_PRIVATE);
                prefs.edit().putString("username", username).putString("password", password).commit();

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences prefs = getActivity().getSharedPreferences(getActivity().getPackageName(),
                                Context.MODE_PRIVATE);
                        prefs.edit().putString("username", "").putString("password", "").commit();
                        login.setText("Login");
                    }
                });
                login.setText("Logout");
                dismiss();
            }

            if (status.failure) {
                Toast.makeText(getActivity(), "Login failed, try again", Toast.LENGTH_SHORT).show();
                login.setText("Login");
            }
        }
    }
}
