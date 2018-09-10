package clover.hamar_bumpy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import clover.hamar_bumpy.Amar_Map;
import clover.hamar_bumpy.PhoneAuthActivity;
import clover.hamar_bumpy.R;


public class RegisterPage extends AppCompatActivity {



    Button next;
   EditText FirstName;
    EditText LastName;
    EditText pass;
    EditText confirmPass;
    TextView already;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
         next=(Button)findViewById(R.id.regButton);
         FirstName=(EditText)findViewById(R.id.FirstName);
         LastName=(EditText)findViewById(R.id.LastName);
         pass=(EditText)findViewById(R.id.pass);
         confirmPass=(EditText)findViewById(R.id.pass);
         already=(TextView)findViewById(R.id.already);
         firebaseAuth=firebaseAuth.getInstance();




        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterPage.this, Amar_Map.class));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ReturenedValue=validate();
                if(!ReturenedValue){
                    Intent i=new Intent(RegisterPage.this,PhoneAuthActivity.class);
                    startActivity(i);


                }
            }
        });

    }
    private Boolean validate(){
        Boolean res=false;
        String fName=FirstName.getText().toString().trim();
        String lName=LastName.getText().toString().trim();
        String pas=pass.getText().toString().trim();
        String confirmPas=confirmPass.getText().toString().trim();

        if((fName.isEmpty()) || (lName.isEmpty()) || (!(pas.equals(confirmPas))) || pas.isEmpty() || confirmPas.isEmpty()) {
            if (fName.isEmpty() || lName.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
            }

           if(pas.isEmpty()){
               Toast.makeText(this, "Enter Password ", Toast.LENGTH_SHORT).show();
           }
           if(confirmPas.isEmpty()){
               Toast.makeText(this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
           }
            if (!(pas.equals(confirmPas))) {
                Toast.makeText(this, "Password does not match with Confirm Password", Toast.LENGTH_SHORT).show();
            }

        }
        else{

            res=true;
        }
        return  res;
    }
}


