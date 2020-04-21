package com.example.firebase_arduino_android_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;

    final DatabaseReference led_blue = myRef.child("/leds/blue_led");
    final DatabaseReference led_green = myRef.child("/leds/green_led");
    final DatabaseReference led_red = myRef.child("/leds/red_led");

    Button btnBlue;
    Button btnRed;
    Button btnGreen;
    TextView textView1;
    boolean bLedBlue, bLedRed, bLedGreen;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        btnBlue = (Button)findViewById(R.id.button1);
        btnRed = (Button)findViewById(R.id.button2);
        btnGreen = (Button)findViewById(R.id.button4);
        final EditText editText = findViewById(R.id.editText);
        textView1 = (TextView)findViewById(R.id.textView1);

        // Firebase anonymous authentication
        mAuth = FirebaseAuth.getInstance();
        signInAnonymously();

        //Create Intent to listen to the speech
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        led_blue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("file", "Value is: " + value);

                if(Objects.equals(value,"ON")) {
                    bLedBlue = true;
                    btnBlue.setBackgroundResource(R.drawable.blue_on);
                }
                else if (Objects.equals(value,"OFF")) {
                    bLedBlue = false;
                    btnBlue.setBackgroundResource(R.drawable.blue_off);
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("file", "Failed to read value.", error.toException());
            }
        });

        led_green.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("file", "Value is: " + value);

                if(Objects.equals(value,"ON")) {
                    bLedGreen = true;
                    btnGreen.setBackgroundResource(R.drawable.green_on);
                }
                else if (Objects.equals(value,"OFF")) {
                    bLedGreen = false;
                    btnGreen.setBackgroundResource(R.drawable.green_off);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("file", "Failed to read value.", error.toException());
            }
        });

        led_red.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("file", "Value is: " + value);

                if(Objects.equals(value,"ON")) {
                    bLedRed = true;
                    btnRed.setBackgroundResource(R.drawable.red_on);
                }
                else if (Objects.equals(value,"OFF")) {
                    bLedRed = false;
                    btnRed.setBackgroundResource(R.drawable.red_off);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("file", "Failed to read value.", error.toException());
            }
        });

        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bLedBlue)
                    led_blue.setValue("OFF");
                else
                    led_blue.setValue("ON");
            }
        });

        btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bLedGreen)
                    led_green.setValue("OFF");
                else
                    led_green.setValue("ON");
            }
        });

        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bLedRed)
                    led_red.setValue("OFF");
                else
                    led_red.setValue("ON");
            }
        });

        //Callback from speech recognizer
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float v) {
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int error) {

                // TODO Auto-generated method stub
                String mError = "";
                String mStatus = "Error detected";
                switch (error) {
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        mError = " network timeout";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                        mError = " network" ;
                        return;
                    case SpeechRecognizer.ERROR_AUDIO:
                        mError = " audio";
                        break;
                    case SpeechRecognizer.ERROR_SERVER:
                        mError = " server";
                        break;
                    case SpeechRecognizer.ERROR_CLIENT:
                        mError = " client";
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        mError = " speech time out" ;
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        mError = " no match" ;
                        break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        mError = " recogniser busy" ;
                        break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        mError = " insufficient permissions" ;
                        break;
                }
                editText.setHint("Error: " +  error + " - " + mError);
            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null) {
                    editText.setText(matches.get(0));
                    String recognizedString = matches.get(0).toLowerCase();

                    //Checks if recognized sentence contains "on" or "off" and the leds color.
                    if (recognizedString.contains("off")){
                        if(recognizedString.contains("red")) {
                            led_red.setValue("OFF");
                            tts.speak("Red off", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        else if (recognizedString.contains("green")) {
                            led_green.setValue("OFF");
                            tts.speak("Green off", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        else if (recognizedString.contains("blue")) {
                            led_blue.setValue("OFF");
                            tts.speak("Blue off", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    else if(recognizedString.contains("on")) {
                        if(matches.get(0).toLowerCase().contains("red")) {
                            led_red.setValue("ON");
                            tts.speak("Red on", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        else if (recognizedString.contains("green")) {
                            led_green.setValue("ON");
                            tts.speak("Green on", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        else if (recognizedString.contains("blue")) {
                            led_blue.setValue("ON");
                            tts.speak("Blue on", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    else if(recognizedString.contains("favorite food")) {
                        tts.speak("Hamburgers!", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        /*when the button is in the pressed state, it will get the speech and start listening,
        and after removing the finger from the button, it will stop listening.*/
        findViewById(R.id.button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //when the user removed the finger
                        mSpeechRecognizer.stopListening();
                        editText.setHint("You will see input here");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        //finger is on the button
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        editText.setText("");
                        editText.setHint("Listening...");
                        break;
                }
                return false;
            }
        });

        //Creates objeto text to speech
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    //tts.setLanguage(new Locale("PT", "BR"));
                    tts.setLanguage(new Locale("EN", "US"));
                }
            }
        });
    }

    /* after Android Marshmallow we have to ask permission to RECORD AUDIO at runtime.*/
    private void checkPermission()
    {
        //Here we are checking if the device is running android marshmallow or ahead
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //if the permission is not granted user will see the settings activity, from where the user can allow the permission needed.
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED))
            {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

     /***********Firebase aUTHENTICATION***********************/
    //When initializing your Activity, check to see if the user is currently signed in.
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    //sign in as an anonymous use
    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("auth", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("auth", "signInAnonymously:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}




