package com.example.principalflee;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class CadastroUser extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText txtEmail, txtSenha, txtContrasenha;
    private Button cadastrar;
    private String senhaPrima, email;
    private String senhaSec;
    private boolean verSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_user);

        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        txtContrasenha = findViewById(R.id.txtConstraSenha);
        cadastrar = findViewById(R.id.cadastrar);


        txtSenha.addTextChangedListener(verificaSenhas);
        txtContrasenha.addTextChangedListener(verificaSenhas);
        txtEmail.addTextChangedListener(verificaSenhas);


        cadastrar.setEnabled(false);
        firebaseAuth = FirebaseAuth.getInstance();

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verSenha){
                    creatUserPassorwdEmail(senhaPrima);
                }else {
                    txtSenha.setText("");
                    txtContrasenha.setText("");
                    txtSenha.requestFocus();
                    Toast.makeText(getApplicationContext(),"Senhas não coincidentes! ",Toast.LENGTH_LONG).show();

                }
            }
        });

    }


    public void creatUserPassorwdEmail(String senhaVerificada) {


        firebaseAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), senhaVerificada).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                   //FirebaseUser user = firebaseAuth.getCurrentUser();
                    Toast.makeText(getApplicationContext(), "Bem-Vindo!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    try { throw Objects.requireNonNull(task.getException());

                    } catch (FirebaseAuthWeakPasswordException e) {
                        //PASSWORD FRACO
                        Toast.makeText(getApplicationContext(), "Senha Fraca!",
                                Toast.LENGTH_SHORT).show();
                        txtSenha.requestFocus();

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        //EMAIL  JA UTILIZADO
                        Toast.makeText(getApplicationContext(), "E-mail invalido!",
                                Toast.LENGTH_SHORT).show();
                        txtEmail.requestFocus();

                    } catch (FirebaseAuthUserCollisionException e) {
                        //USUARAIO JA ADICIONADO
                        Toast.makeText(getApplicationContext(), "Usuário existente!",
                                Toast.LENGTH_SHORT).show();
                        txtEmail.requestFocus();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FirebaseAuth.getInstance().signOut();
        Log.d("con", "destruido");
    }

    private TextWatcher verificaSenhas = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            senhaPrima = txtSenha.getText().toString().trim();
            senhaSec = txtContrasenha.getText().toString().trim();
            email = txtEmail.getText().toString().trim();
            verSenha = false;

            if ( !senhaPrima.isEmpty() && !email.isEmpty() && !senhaSec.isEmpty()) {
                //if(!senhaPrima.isEmpty() && !senhaSec.isEmpty() && txtSenha.equals(txtContrasenha) ) {


                //cadastrar.setEnabled(true);
                //}
                cadastrar.setEnabled(true);
            } else {
                cadastrar.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if(senhaPrima.equalsIgnoreCase(senhaSec)){
                verSenha = true;
            }


        }

    };
}


