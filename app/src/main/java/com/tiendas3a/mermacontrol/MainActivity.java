
package com.tiendas3a.mermacontrol;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.PermissionRequest;

public class MainActivity extends Activity {

    private WebView webView;
    private PermissionRequest cameraRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(0xFF10243E);
        getWindow().setNavigationBarColor(0xFF10243E);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                boolean camera = false;

                for (String resource : request.getResources()) {
                    if (PermissionRequest.RESOURCE_VIDEO_CAPTURE.equals(resource)) {
                        camera = true;
                    }
                }

                if (camera &&
                    checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                    cameraRequest = request;
                    requestPermissions(
                        new String[]{Manifest.permission.CAMERA}, 100
                    );

                } else {
                    request.grant(request.getResources());
                }
            }
        });

        if (checkSelfPermission(Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                new String[]{Manifest.permission.CAMERA}, 100
            );
        }

        webView.loadUrl(
            "https://fabriflores331-cpu.github.io/MermaControl/"
        );
    }

    @Override
    public void onRequestPermissionsResult(
        int requestCode,
        String[] permissions,
        int[] results
    ) {
        super.onRequestPermissionsResult(
            requestCode, permissions, results
        );

        if (requestCode == 100 && cameraRequest != null) {
            if (results.length > 0 &&
                results[0] == PackageManager.PERMISSION_GRANTED) {
                cameraRequest.grant(cameraRequest.getResources());
            } else {
                cameraRequest.deny();
            }

            cameraRequest = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
