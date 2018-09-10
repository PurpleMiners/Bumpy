package clover.hamar_bumpy;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MainActivity extends AppCompatActivity {
    public static DatabaseReference myRef;
    EditText name;
    EditText pass;
    Button signInButton;
    Button regButton;
    TextView info;
    public int counter=5;
    static AnimationDrawable animationDrawable;
    public static User user;
    static View pb,whitebackg;
    ValueEventListener valueEventListener;
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout constraintLayout=findViewById(R.id.mainLayout);
        animationDrawable=(AnimationDrawable)constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();
        name=(EditText)findViewById(R.id.userName);
        pass=(EditText)findViewById(R.id.pass);
        signInButton=(Button)findViewById(R.id.signInButton);
        regButton=(Button)findViewById(R.id.regButton);
        info=(TextView)findViewById(R.id.info);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

//        info.setText("Number of Attemps remaining: "+Integer.toString(5));

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                myRef.addListenerForSingleValueEvent(valueEventListener);
                pb=findViewById(R.id.progressBar2);
                pb.setVisibility(View.VISIBLE);
                whitebackg=findViewById(R.id.whiteBack);
                whitebackg.setVisibility(View.VISIBLE);
                final AnimationDrawable animationDrawable=(AnimationDrawable)pb.getBackground();
                animationDrawable.setExitFadeDuration(50);
                animationDrawable.setExitFadeDuration(50);
                animationDrawable.start();
                signInButton.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        validate(name.getText().toString(),pass.getText().toString());
                        pb.setVisibility(View.INVISIBLE);
                        whitebackg.setVisibility(View.INVISIBLE);
                        signInButton.setVisibility(View.VISIBLE);
                        animationDrawable.stop();
                    }
                },3000);

            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationDrawable.stop();
                startActivity(new Intent(MainActivity.this,PhoneAuthActivity.class));
            }
        });

        valueEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    user=(User)dataSnapshot.child("users").child(name.getText().toString()).getValue(User.class);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    void validate(final String userName, String userPass){

        myRef.removeEventListener(valueEventListener);

        if(user!=null) {
            if (user.pwd.equals(userPass)) {
                animationDrawable.stop();
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,Amar_Map.class));
            } else {
                counter--;
                info.setText("Number of Attemps remaining: " + Integer.toString(counter));
                if (counter == 0) {
                    info.setText("Too Many attemps");
                    signInButton.setEnabled(false);
                }
            }
        }
        else{
            counter--;
            info.setText("Number of Attemps remaining: "+Integer.toString(counter));
            if(counter==0){
                info.setText("Too Many attemps");
                signInButton.setEnabled(false);
            }
        }
    }
}




//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();
//
//        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                EditText edt=findViewById(R.id.editText2);
//                EditText tdt=findViewById(R.id.editText3);
//                String name=edt.getText().toString();
//                String pass=tdt.getText().toString();
//                User user = new User(name, pass,Integer.toString(++id));
//                myRef.child("users").child(Integer.toString(id)).setValue(user);
//
//
//                ValueEventListener postListener = new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // Get Post object and use the values to update the UI
////                        Post post = dataSnapshot.getValue(Post.class);
//                        User user1=(User) dataSnapshot.child("users").child(Integer.toString(id)).getValue(User.class);
//                        // ...
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        // Getting Post failed, log a message
//                        Log.w("", "loadPost:onCancelled", databaseError.toException());
//                        // ...
//                    }
//                };
//                myRef.addValueEventListener(postListener);
//            }
//        });
//    }
