package com.nachiketaprasad4.myvoice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech myTTS;
    private SpeechRecognizer mySpeechRecognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        EditText edit_input=(EditText ) findViewById(R.id.editText_input);
        EditText edit_results = (EditText  )findViewById(R.id.editText_result);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySpeechRecognizer.startListening(intent);
            }
        });
        IntializeTextToSpeech();
        InializeSpeechREcognizer(edit_input,edit_results);
    }

    private void InializeSpeechREcognizer(final EditText edit_input, final EditText edit_results) {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            mySpeechRecognizer =  SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
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
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> results =bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if(results!=null){
                        edit_input.setText(results.get(0));
                    }
                    processResult(results.get(0),edit_results);
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }
    }

    private void processResult(String command, EditText edit_results) {
        command =command.toLowerCase();
        if(command.indexOf("what") !=-1){
            if(command.indexOf("time")!=-1){
                Date now=new Date();
                String time = DateUtils.formatDateTime(this,now.getTime(),DateUtils.FORMAT_SHOW_TIME);

                speak("The time now is"+time);
                edit_results.setText("The time now is"+time);
            }
        }else if(command.indexOf("joke")!=-1){
            speak("Teacher: Why are you late? \n" +
                    "Student: There was a man who lost a hundred dollar bill. \n" +
                    "Teacher: That's nice. Were you helping him look for it? \n" +
                    "Student: No. I was standing on it.");
            edit_results.setText("Teacher: Why are you late? \n" +
                    "Student: There was a man who lost a hundred dollar bill. \n" +
                    "Teacher: That's nice. Were you helping him look for it? \n" +
                    "Student: No. I was standing on it.");

        }

    }

    private void IntializeTextToSpeech() {
            myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    if(myTTS.getEngines().size()==0){
                        Toast.makeText(MainActivity.this,"There is no TTS Engine",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        myTTS.setLanguage(Locale.US);
                         speak("Hello,I am ready");
                    }
                }
            });
    }

    private void speak(String message) {
        if(Build.VERSION.SDK_INT >21){
            myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
        }else{
            myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myTTS.shutdown();
    }
}
