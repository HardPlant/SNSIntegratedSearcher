package kr.jnu.embedded.snssearcher.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.media.Image;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

import kr.jnu.embedded.snssearcher.data.FacebookPage;
import kr.jnu.embedded.snssearcher.data.FacebookPagePost;

/**
 * Created by KANG on 2017-11-22.
 */

public class FacebookSearcherPresenter implements SNSSearcherContract.Presenter {
    public static final String TAG = "FacebookSearcher";

    private AccessToken accessToken;

    private SNSSearcherContract.View view;

    ArrayList<FacebookPage> pages = new ArrayList<>();
    ArrayList<FacebookPagePost> posts = new ArrayList<>();

    @Override
    public void setView(SNSSearcherContract.View view) {
        this.view = view;
    }

    @Override
    public void loadItem(final SNSSearcherContract.LoadCompleteListner listener) {
        FacebookPagePostFetcher facebookPagePostFetcher = new FacebookPagePostFetcher(accessToken
                , new FacebookPagePostFetcher.OnCompleteListener() {
            @Override
            public void onComplete(ArrayList<JSONObject> pages, ArrayList<JSONObject> postArray) {
                Log.d(TAG, "Page Post Fetch completed.");
                Log.d(TAG, "Fetched Pages: " + pages.toString());
                Log.d(TAG, "Fetched Posts: " + postArray.toString());
                parsePages(pages, postArray);

                Log.d(TAG, "Result Posts: " + posts.toString());
                listener.onComplete(posts);
            }
        });

        facebookPagePostFetcher.start();
        Log.d(TAG, "Page Post Fetch started.");
    }

    public void parsePages(ArrayList<JSONObject> pageInfo, ArrayList<JSONObject> fetchedPageResult){
        try {
            for(JSONObject page : pageInfo) {
                    String name = page.getString("name");
                    String id = page.getString("id");
                    String picture = page.getJSONObject("picture").getJSONObject("data").getString("url");
                    pages.add(new FacebookPage(id, picture, name));

            }

            for(JSONObject object : fetchedPageResult) {
                Log.d(TAG, "Page Object: " + object);
                for (Iterator<String> itr = object.keys(); itr.hasNext(); ){
                    String key = (String)itr.next();
                    JSONObject item = (JSONObject) object.get(key);
                    JSONArray data = item.getJSONArray("data");
                    Log.d(TAG, "ITEM:" + item);
                    Log.d(TAG, "DATA:" + data);
                    FacebookPage facebookPage = findPagebyId(key, pages);

                    for(int i=0; i<data.length(); i++){
                        String message = data.getString(i);
                        posts.add(new FacebookPagePost(facebookPage, message));
                    }
                }
            }
        } catch(JSONException je){
            je.printStackTrace();
        }

    }
    private FacebookPage findPagebyId(String key, ArrayList<FacebookPage> pages){
        for(FacebookPage page : pages){
            if(page.getID().equals(key)) return page;
        }
        return null;
    }


    public FacebookSearcherPresenter() {
        CallbackManager mCallbackManager;
        AccessTokenTracker accessTokenTracker;

        mCallbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
            }
        };
        accessTokenTracker.startTracking();
        accessToken = AccessToken.getCurrentAccessToken();
    }


}
