package sg.edu.nus.comp.orbital.synchro;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Handler handlerForJavascriptInterface = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUser();
    }

    //launch the main activity, if the activity exist in backstack, bring it to front instead of
    //creating new instance
    private void redirectUser() {
        Intent launchMainActivity = new Intent(LoginActivity.this, DrawerActivity.class);
        launchMainActivity.putExtra("caller", "LoginActivity");
        launchMainActivity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        LoginActivity.this.startActivity(launchMainActivity);
        finish();
    }

    //method for login
    //redirects user after authentication
    private void loginUser() {
        WebView webview = (WebView) findViewById(R.id.webview_login);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");

        webview.loadUrl(SynchroAPI.ivleLogin);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                //this sets webview to invisible ONLY if not the login page to IVLE
                if (!url.equals(SynchroAPI.ivleLogin)) {
                    view.setVisibility(View.GONE);

                    /*  user validated
                        extract token from html
                        redirects to landing page
                    */
                    if (url.equals(SynchroAPI.ivleLoginSuccess)) {
                        view.loadUrl("javascript:window.HtmlViewer.showHTML" +
                                "('&lt;html&gt;'+document.getElementsByTagName('html')[0].innerHTML+'&lt;/html&gt;');");
                        redirectUser();

                    } else {  //in case anything else happens
                        Log.d("Synchro", "Something is wrong!");
                    }

                }

            }

            //if there's connection issue or some other error
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                view.setVisibility(View.GONE);

                Snackbar notifyInternet = Snackbar.make(view, "Internet please", Snackbar.LENGTH_INDEFINITE);
                notifyInternet.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent launchLoginActivity = new Intent(LoginActivity.this, LoginActivity.class);
                        launchLoginActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(launchLoginActivity);
                        finish();
                    }
                });
                notifyInternet.show();
            }

        });
    }

    //for extracting html source code and saving token to SharedPrefs
    class MyJavaScriptInterface
    {
        private Context ctx;

        MyJavaScriptInterface(Context ctx)
        {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(final String html)
        {
            //code to use html content here
            handlerForJavascriptInterface.post(new Runnable() {
                @Override
                public void run()
                {
                    String[] splitTokens = html.split("<body>");
                    String token = splitTokens[1];
                    splitTokens = token.split("</body>");
                    String ivleToken = splitTokens[0];

                    AuthToken.setToken(ivleToken);

                    //for debug
                    //Log.d("Synchro", ivleToken);
                }});
        }
    }


}
