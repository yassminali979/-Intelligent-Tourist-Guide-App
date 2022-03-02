package com.example.intelligenttourist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {
    Button l;
    TextView f;
    EditText u,p;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.logintabfragment,container,false);
        l=root.findViewById(R.id.login);
        u=root.findViewById(R.id.usermail);
        p=root.findViewById(R.id.pass);
        f=root.findViewById(R.id.forget);
        mAuth = FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        Log.d("TAG","onClick" + u.getText().toString());
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = u.getText().toString();
                String password = p.getText().toString();
                Log.d("TAG","onclick:"+email);
                if (email.isEmpty()) {
                    u.setError("Please enter email!!");
                    return;
                }
                if (password.isEmpty()) {
                    p.setError("Please enter password!!");
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getContext(), "Login successful!!", Toast.LENGTH_LONG).show();
                        checkUserAccessLevel(authResult.getUser().getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),ForgetPassword.class));
            }
        });

        return root;
    }
    private void checkUserAccessLevel(String uid) {
        DocumentReference df=fstore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onSuccess:" + documentSnapshot.getData());
                if(documentSnapshot.getString("isAgency")!=null)
                {
                    startActivity(new Intent(getContext(),Agency.class));
                }
                if(documentSnapshot.getString("isUser")!=null)
                {
                    startActivity(new Intent(getContext(),Userlog.class));
                }
            }
        });

    }

}

