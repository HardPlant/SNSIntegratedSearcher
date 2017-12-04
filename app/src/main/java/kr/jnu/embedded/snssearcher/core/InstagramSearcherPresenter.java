package kr.jnu.embedded.snssearcher.core;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.ArrayList;

import kr.jnu.embedded.snssearcher.base.App;

/**
 * Created by KANG on 2017-12-03.
 */

public class InstagramSearcherPresenter implements SNSSearcherContract.Presenter {
    private static final String TAG = "InstaSearcherPresenter";
    private SNSSearcherContract.View view;
    private String tag;
    private InstagramSearcher instagramSearcher;
    private App application;


    public InstagramSearcherPresenter(Context context) {
        application = (App)context.getApplicationContext();
        instagramSearcher = new InstagramSearcher(application.getInstagramAccessToken());
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void loadItem(SNSSearcherContract.LoadCompleteListner listener) {
        ArrayList<Object> result = new ArrayList<>();
        Log.d(TAG, "loaditem call");

        if(isAccessTokenSet()){
            Log.d(TAG, "AccessToken is null");
            listener.onComplete(null);
            return;
        }

        result.addAll(instagramSearcher.getHashTagMedia(tag));

        listener.onComplete(result);
    }

    @Override
    public void setView(SNSSearcherContract.View view) {
        this.view = view;
    }

    public boolean isAccessTokenSet(){
        if(application.getInstagramAccessToken() == null) return false;

        return true;
    }
}
