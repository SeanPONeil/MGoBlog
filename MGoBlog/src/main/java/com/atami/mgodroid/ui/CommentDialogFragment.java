package com.atami.mgodroid.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.activeandroid.query.Select;
import com.atami.mgodroid.R;
import com.atami.mgodroid.events.CommentPostTaskStatus;
import com.atami.mgodroid.io.CommentPostTask;
import com.atami.mgodroid.io.LoginTask;
import com.atami.mgodroid.models.CommentJsonObj;
import com.atami.mgodroid.models.User;
import com.atami.mgodroid.ui.base.BaseDialogFragment;
import com.squareup.otto.Subscribe;
import com.squareup.tape.TaskQueue;

import javax.inject.Inject;

public class CommentDialogFragment extends BaseDialogFragment {

    int pid;
    int nid;

    ProgressDialog mProgressDialog;

    @Inject
    TaskQueue<LoginTask> loginQueue;

    @Inject
    TaskQueue<CommentPostTask> commentQueue;

    public static CommentDialogFragment newInstance(int pid, int nid) {
        CommentDialogFragment f = new CommentDialogFragment();

        Bundle args = new Bundle();
        args.putInt("pid", pid);
        args.putInt("nid", nid);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pid = getArguments().getInt("pid");
        nid = getArguments().getInt("nid");
        if (savedInstanceState == null) {
            //Refresh session id before posting
            SharedPreferences prefs = getActivity().getSharedPreferences(getActivity().getPackageName(),
                    Context.MODE_PRIVATE);
            loginQueue.add(new LoginTask(prefs.getString("username", null), prefs.getString("password", null),
                    getTag()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.comment, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ImageButton submit = (ImageButton) getView().findViewById(R.id.cmt_sumbit);
        final EditText subject = (EditText) getView().findViewById(R.id.cmt_title);
        final EditText comment = (EditText) getView().findViewById(R.id.cmt_body);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getSharedPreferences(getActivity().getPackageName(),
                        Context.MODE_PRIVATE);
                String username = prefs.getString("username", null);
                if (username == null) {
                    Toast.makeText(getActivity(), "Login to MGoBlog to comment", Toast.LENGTH_SHORT).show();
                } else {
                    String subjectText = subject.getText().toString();
                    String commentText = comment.getText().toString();
                    if(TextUtils.isEmpty(commentText)){
                        Toast.makeText(getActivity(), "Enter a comment", Toast.LENGTH_SHORT).show();
                        return;
                    } else if(TextUtils.isEmpty(subjectText)) {
                    	//get first 50 characters from comment
                    	int max_subject = 50;
                    	boolean cutAtWord = false;
                    	
                    	if(commentText.length() > max_subject) {
                    		subjectText = commentText.substring(0, max_subject);
                    		if(commentText.charAt(max_subject) == ' ')
                    			cutAtWord = true;
                    	} else {
                    		subjectText = commentText;
                    	}

                    	//remove trailing letters if not cut off at a space
                    	int last_space = subjectText.lastIndexOf(" ");
                    	
                    	if(last_space > 0 && !cutAtWord) {
                    		subjectText = subjectText.substring(0, last_space);
                    	}	
                    }
                    User user = new Select().from(User.class).where("name = ?", username).executeSingle();
                    CommentJsonObj payload = new CommentJsonObj(subject.getText().toString(),
                            comment.getText().toString(), String.valueOf(user.getUid()), String.valueOf(pid),
                            String.valueOf(nid));
                    commentQueue.add(new CommentPostTask(payload, user.getUid(), getTag()));
                }
            }
        });
    }

    @Subscribe
    public void onCommentPostTaskStatus(CommentPostTaskStatus status) {
        if (status.tag.equals(getTag())) {
            if (status.running) {
                if(mProgressDialog == null){
                    mProgressDialog = new ProgressDialog(getActivity());
                    mProgressDialog.setIndeterminate(true);
                }
                mProgressDialog.setMessage("Posting comment...");
                mProgressDialog.show();
            } else {
                if(mProgressDialog == null){
                    mProgressDialog = new ProgressDialog(getActivity());
                    mProgressDialog.setIndeterminate(true);
                }
                mProgressDialog.hide();
            }

            if(status.completed){
                mProgressDialog.hide();
                dismiss();
            }
        }
    }
}