package kr.jnu.embedded.snssearcher.ui.activities;

import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import kr.jnu.embedded.snssearcher.R;
import kr.jnu.embedded.snssearcher.core.InstagramSearcherPresenter;
import kr.jnu.embedded.snssearcher.core.SNSSearcherContract;

public class InstagramActivity extends AppCompatActivity {
    InstagramSearcherPresenter presenter;
    Button button;
    AlertDialog result;
    private static final String TAG = "InstagramActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);
        button = findViewById(R.id.instaButton);

        presenter = new InstagramSearcherPresenter(this);
        ResultView resultView = new ResultView();
        resultView.setPresenter(presenter);
        //resultView.updateItem();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog(){
        Uri uri = Uri.parse(presenter.getTokenUrl());
        WebView webView = new WebView(this);
        webView.loadUrl(uri.toString());
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, url);
                if(url.contains("access_token=")) {
                    presenter.setAccessTokeFromLoginResponse(url);
                    result.cancel();
                }
                return false;
            }
        });
        dialog.setView(webView);
        result = dialog.create();
        result.create();
    }

    public class ResultView implements SNSSearcherContract.View{
        SNSSearcherContract.Presenter presenter;
        TextView textView;

        public ResultView() {
            textView = findViewById(R.id.instaResultView);

        }

        @Override
        public void setPresenter(SNSSearcherContract.Presenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public void updateItem() {
            textView.setText(
                ((InstagramSearcherPresenter)presenter).getTokenUrl()
            );
        }
    }
}
