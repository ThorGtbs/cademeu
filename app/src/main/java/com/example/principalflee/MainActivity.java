package com.example.principalflee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Classes.EmailSenhaUser;

public class MainActivity extends AppCompatActivity{
    // firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
   // componentes
    private Button entrar;
    private EditText email, senha;
    private TextView cadastroBtn, esqueceuBtn;
    private EmailSenhaUser emailSenhaUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senha = findViewById(R.id.password);
        email = findViewById(R.id.username);
        entrar = findViewById(R.id.login);
        cadastroBtn = findViewById(R.id.cadBotao);
        esqueceuBtn = findViewById(R.id.esqBotao);
        emailSenhaUser = new EmailSenhaUser();

        senha.addTextChangedListener(verificaDadosEntrada);
        email.addTextChangedListener(verificaDadosEntrada);

        firebaseAuth = FirebaseAuth.getInstance();
        entrar.setEnabled(false);
    }

    public void sign(){

        emailSenhaUser.setEmail(email.getText().toString());
        emailSenhaUser.setSenha(senha.getText().toString());

        firebaseAuth.signInWithEmailAndPassword(emailSenhaUser.getEmail(),emailSenhaUser.getSenha()).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("con", "conectado");
                    //FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    Intent it = new Intent(getApplicationContext(), MapsActivity

                            .class);
                    startActivity(it);

                }else{
                    Log.d("con", "desconectado");
                    Toast.makeText( getApplicationContext(),"E-mail ou Senha errados!" ,Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!= null){
            startActivity(new Intent( this, MapsActivity.class));
        }
       //updateUI(firebaseUser);
          */
    }

    private void updateUI(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
        if (firebaseUser.getUid() ==  firebaseAuth.getUid()){
           Intent itProfile = new Intent(this, Profile.class);
           startActivity(itProfile);
       }
    }


    public void iniciaCadastro(){
        Intent intent = new Intent(this,CadastroUser.class);
        startActivity(intent);
    }
    public void iniciaRecSenha(){
        Intent it = new Intent(this, RecuperacaoSenha.class);
        startActivity(it);
    }

    public void onCLick( View v) {
        switch (v.getId()) {
            case R.id.login:
                sign();
                break;
            case R.id.cadBotao:
                iniciaCadastro();
                break;
            case R.id.esqBotao:
                iniciaRecSenha();
                break;
            default:
                break;
        }
    }
    /*
    @Override
    protected void onDestroy(){
        super.onDestroy();
        firebaseAuth.getInstance().signOut();

    }*/

    private TextWatcher verificaDadosEntrada = new TextWatcher(){


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String inputEmail = email.getText().toString().trim();
            String inputSenha = senha.getText().toString().trim();
            Log.d("con", "p");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                if(!inputEmail.isEmpty() && !inputSenha.isEmpty()) {
                    entrar.setEnabled(true);
                }else {
                    entrar.setEnabled(false);
                }
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}
