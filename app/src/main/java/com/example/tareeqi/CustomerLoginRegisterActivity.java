package com.example.tareeqi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerLoginRegisterActivity extends AppCompatActivity {

    private Button CustomerLoginButton;
    private Button CustomerRegisterButton;
    private TextView CustomerStatus;
    private TextView CustomerRegisterLink;
    private EditText EmailCustomer;
    private EditText PasswordCustomer;
    private EditText PasswordCustomer2;
    private ProgressBar CustomerBar;
    private String OnlineCustomerID;


    private FirebaseAuth mAuth;

    //1-to check if the user is logged in or to get a specific session
    //2-by a way or another when you create a new account from it it generates a unique id
    private DatabaseReference CustomerDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login_register);

        mAuth=FirebaseAuth.getInstance();
        //OnlineCustomerID=mAuth.getCurrentUser().getUid();
        //CustomerDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(OnlineCustomerID);

        CustomerLoginButton =findViewById(R.id.customer_login_btn);
        CustomerRegisterButton =findViewById(R.id.customer_register_btn);
        CustomerStatus =findViewById(R.id.customer_status);
        CustomerRegisterLink =findViewById(R.id.register_customer_link);
        EmailCustomer=findViewById(R.id.email_customer);
        PasswordCustomer=findViewById(R.id.password_customer);
        PasswordCustomer2=findViewById(R.id.password_customer2);
        CustomerBar=findViewById(R.id.customer_bar);



        PasswordCustomer2.setVisibility(View.INVISIBLE);
        CustomerRegisterButton.setVisibility(View.INVISIBLE);
        CustomerRegisterButton.setEnabled(false);

        CustomerRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordCustomer2.setVisibility(View.VISIBLE);
                CustomerLoginButton.setVisibility(View.INVISIBLE);
                CustomerRegisterLink.setVisibility(View.INVISIBLE);
                CustomerStatus.setText("Register customer");
                PasswordCustomer2.setVisibility(View.VISIBLE);
                CustomerRegisterButton.setVisibility(View.VISIBLE);
                CustomerRegisterButton.setEnabled(true);


            }
        });

        CustomerRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=EmailCustomer.getText().toString();
                String password=PasswordCustomer.getText().toString();

 RegisterCustomer(email,password);


            }
        });


        CustomerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=EmailCustomer.getText().toString();
                String password=PasswordCustomer.getText().toString();

                SignInCustomer(email,password);
            }
        });


    }



    private void SignInCustomer(String email, String password) {
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(CustomerLoginRegisterActivity.this,"Please Enter Email...",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(CustomerLoginRegisterActivity.this,"Please Enter Password...",Toast.LENGTH_SHORT).show();
        }

        else
        {
            CustomerBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {

                        Intent intent=new Intent(CustomerLoginRegisterActivity.this,CustomersMapActivity.class);
                        intent.putExtra("type","Customers");
                        startActivity(intent);
                        Toast.makeText(CustomerLoginRegisterActivity.this,"Customer Signed In successful",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(CustomerLoginRegisterActivity.this,"Signing In Unsuccessful, please try again...",Toast.LENGTH_SHORT).show();
                    }
                    CustomerBar.setVisibility(View.INVISIBLE);
                }
            });
        }

    }



    private void RegisterCustomer(String email, String password) {

        if(TextUtils.isEmpty(email))  //front end check
        {
            Toast.makeText(CustomerLoginRegisterActivity.this,"Please Enter Email...",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(CustomerLoginRegisterActivity.this,"Please Enter Password...",Toast.LENGTH_SHORT).show();
        }
        if(!(PasswordCustomer2.equals(PasswordCustomer))){
            Toast.makeText(CustomerLoginRegisterActivity.this,"Password does not match...",Toast.LENGTH_SHORT).show();
        }
        else //back end check
        {
            CustomerBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())//to make sure that the user inputs a proper email and password for the DB
                    {//note : unique ID is just created
                        OnlineCustomerID=mAuth.getCurrentUser().getUid();//to get the ID that is just created
                        CustomerDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(OnlineCustomerID);//where to save

                        CustomerDatabaseRef.setValue(true);//create that root and save

                        Intent intent=new Intent(CustomerLoginRegisterActivity.this,CustomersMapActivity.class);
                        intent.putExtra("type","Customers");
                        startActivity(intent);
                        Toast.makeText(CustomerLoginRegisterActivity.this,"Customer registered successful",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(CustomerLoginRegisterActivity.this,"Registration Unsuccessful, please try again...",Toast.LENGTH_SHORT).show();
                    }
                    CustomerBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}