package org.ei.drishti.view.activity;

import android.content.Intent;
import android.webkit.WebSettings;
import org.apache.commons.io.IOUtils;
import org.ei.drishti.Context;

import java.io.IOException;
import java.text.MessageFormat;

import static java.util.UUID.randomUUID;
import static org.ei.drishti.AllConstants.*;
import static org.ei.drishti.util.Log.logError;

public class FormActivity extends SecuredWebActivity {
    private String model;
    private String form;
    private String formName;

    @Override
    protected void onInitialization() {
        try {
            getIntentData();
        } catch (IOException e) {
            logError(e.toString());
            finish();
        }

        webViewInitialization();
    }

    private void getIntentData() throws IOException {
        Intent intent = getIntent();
        formName = intent.getStringExtra(FORM_NAME_PARAM);
        model = IOUtils.toString(getAssets().open("www/form/" + formName + "/model.xml"));
        form = IOUtils.toString(getAssets().open("www/form/" + formName + "/form.xml"));
    }

    private void webViewInitialization() {
        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new FormWebInterface(model, form), "androidContext");
        webView.addJavascriptInterface(Context.getInstance().formDataService(), "formDataRepositoryContext");
        webViewSettings.setDatabaseEnabled(true);
        webViewSettings.setDomStorageEnabled(true);
        webView.loadUrl(MessageFormat.format("file:///android_asset/www/form/template.html?{0}={1}&{2}={3}&{4}={5}",
                FORM_ID_PARAMETER, formName, ENTITY_ID_PARAM, "df8e94dd-91bd-40d2-a82a-fb7402e97f30", INSTANCE_ID_PARAM, randomUUID()));
    }
}
