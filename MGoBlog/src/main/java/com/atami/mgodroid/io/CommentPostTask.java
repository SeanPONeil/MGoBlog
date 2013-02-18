package com.atami.mgodroid.io;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.activeandroid.query.Select;
import com.atami.mgodroid.models.CommentJsonObj;
import com.atami.mgodroid.models.Session;
import com.atami.mgodroid.models.User;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.squareup.tape.Task;

import static com.github.kevinsawicki.http.HttpRequest.CHARSET_UTF8;

public class CommentPostTask implements Task<CommentPostTask.Callback> {

    public final static String TAG = "CommentPostTask";

    private final static String URL = "http://mgoblog.com/mobileservices/comment.json";
    private final static Handler MAIN_THREAD = new Handler(Looper.getMainLooper());

    CommentJsonObj payload;
    int uid;
    String tag;

    public CommentPostTask(CommentJsonObj payload, int uid, String tag) {
        this.payload = payload;
        this.uid = uid;
        this.tag = tag;
    }

    public interface Callback {
        void onSuccess();

        void onFailure();
    }

    @Override
    public void execute(final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Construct cookie header and add it to request
                    User user = new Select().from(User.class).where("uid = ?", uid).executeSingle();
                    Session session = new Select().from(Session.class).where("user = ?", user.getId()).executeSingle();
                    String cookie = new StringBuilder().append(session.getSessionName()).append("=").append(session
                            .getSessid()).toString();
                    Log.i(TAG, cookie);
                    HttpRequest request = new HttpRequest(URL, "POST").header("Content-Type",
                            "application/json").header("Accept-Charset", "UTF-8").header("Cookie", cookie);
                    Log.i(TAG, new Gson().toJson(payload));
                    request.send(new Gson().toJson(payload));

                    if (request.ok()) {
                        Log.i(TAG, "Comment post success!");

                        // Get back to the main thread before invoking a callback.
                        MAIN_THREAD.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess();
                            }
                        });
                    } else {
                        Log.i(TAG, "Comment post failed.");
                        Log.i(TAG, request.body());
                        Log.i(TAG, String.valueOf(request.code()));
                        Log.i(TAG, request.message());

                        // Get back to the main thread before invoking a callback.
                        MAIN_THREAD.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure();
                            }
                        });
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }).start();
    }

    public String getTag(){
        return tag;
    }

}
