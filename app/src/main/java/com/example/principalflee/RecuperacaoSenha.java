package com.example.principalflee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class RecuperacaoSenha extends AppCompatActivity {

    FirebaseAuth auth;
    EditText email;
    View contextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacao_senha);
        email = findViewById(R.id.txtEmail);
        contextView = findViewById(R.id.view);
        //verificaEmail();
        redefinirSenhar();
    }



    public void  redefinirSenhar(){
        auth = FirebaseAuth.getInstance();
        auth.setLanguageCode("pt");


        auth.sendPasswordResetEmail("gabrieltorquato@hotmail.com")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Log.d("email", "Email sent.");
                            Snackbar.make(contextView, "Um link foi" +
                                    " enviado para o E-mail digitado", Snackbar.LENGTH_INDEFINITE).setAction("Entendi!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // abrir caixa de dialogo com os apps
                                    Log.i("create", "entrou");
                                    implicitIntent();

                                }
                            }).show();
                        }

                    }
                });
    }

    public void implicitIntent(){

        /*
        Intent intent = new Intent();
        //intent.setAction(Intent.ACTION_MAIN);
        //intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.makeMainSelectorActivity(Intent.ACTION_VIEW, Intent.CATEGORY_APP_EMAIL);
        intent.setType("message/rfc822");

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent,packageManager.MATCH_DEFAULT_ONLY);

        String title = getResources().getString(R.string.string_implict_intent);
        Intent chooserPainel = Intent.createChooser(intent,title);
        if(activities.size() > 0 ){
            Log.i("create", "entrou 2");
            startActivity(chooserPainel);

       }*/
        Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"));
        PackageManager pm = getPackageManager();

        List<ResolveInfo> resInfo = pm.queryIntentActivities(emailIntent, 0);
        if (resInfo.size() > 0) {
            ResolveInfo ri = resInfo.get(0);
            // First create an intent with only the package name of the first registered email app
            // and build a picked based on it
            Intent intentChooser = pm.getLaunchIntentForPackage(ri.activityInfo.packageName);
            Intent openInChooser =
                    Intent.createChooser(intentChooser,"aaa");

            // Then create a list of LabeledIntent for the rest of the registered email apps
            List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
            for (int i = 1; i < resInfo.size(); i++) {
                // Extract the label and repackage it in a LabeledIntent
                ri = resInfo.get(i);
                String packageName = ri.activityInfo.packageName;
                Intent intent = pm.getLaunchIntentForPackage(packageName);
                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }

            LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);
            // Add the rest of the email apps to the picker selection
            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
            startActivity(openInChooser);
        }
    }



}
