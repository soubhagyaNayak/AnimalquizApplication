package com.example.animalquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String GUESSES ="settings_numberOfGuesses";
    public static final String ANIMALS_TYPE="settings_animalsType";
    public static final String QUIZ_BACKGROUND_COLOR="settings_quiz_background_color";
    public static final String QUIZ_FONT="setting_quiz_font";

    private  boolean isSettingsChanged=false;

    static Typeface chunkFive,fontleroyBrown,wonderBarDemo;

    MainActivityFragment myAnimalQuizFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chunkFive=Typeface.createFromAsset(getAssets(),"fonts/Chunkfive.otf");
        fontleroyBrown=Typeface.createFromAsset(getAssets(),"fonts/FontleroyBrown.ttf");
        wonderBarDemo=Typeface.createFromAsset(getAssets(),"fonts/Wonderbar Demo.otf");

        PreferenceManager.setDefaultValues(this,R.xml.quiz_preferences,false);

        PreferenceManager.getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(settingsChangedListener);

        myAnimalQuizFragment=(MainActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.animalQuizFragment);


        myAnimalQuizFragment.modifyAnimalsGuessRows(PreferenceManager.getDefaultSharedPreferences(MainActivity.this));
       myAnimalQuizFragment.modifyTypeOfAnimalInQuiz(PreferenceManager.getDefaultSharedPreferences(this));
       myAnimalQuizFragment.modifyQuizFont(PreferenceManager.getDefaultSharedPreferences(MainActivity.this));
       myAnimalQuizFragment.modifyBackgroundColor(PreferenceManager.getDefaultSharedPreferences(this));
       myAnimalQuizFragment.resetAnimalQuiz();
       isSettingsChanged=false;

    }

    private SharedPreferences.OnSharedPreferenceChangeListener settingsChangedListener=new
            SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                    isSettingsChanged=true;
                    if(key.equals(GUESSES)){
                        myAnimalQuizFragment.modifyAnimalsGuessRows(sharedPreferences);
                        myAnimalQuizFragment.resetAnimalQuiz();

                    }else if(key.equals(ANIMALS_TYPE)){

                        Set<String> animalTypes=sharedPreferences.getStringSet(ANIMALS_TYPE,null);

                        if(animalTypes!=null &&animalTypes.size()>0){


                            myAnimalQuizFragment.modifyTypeOfAnimalInQuiz(sharedPreferences);
                            myAnimalQuizFragment.resetAnimalQuiz();

                        }else{

                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            animalTypes.add(getString(R.string.default_animal_type));
                            editor.putStringSet(ANIMALS_TYPE,animalTypes);

                            Toast.makeText(MainActivity.this, R.string.toast_message,
                                    Toast.LENGTH_SHORT).show();
                            editor.apply();




                        }

                    }else if(key.equals(QUIZ_FONT)){

                        myAnimalQuizFragment.modifyQuizFont(sharedPreferences);
                        myAnimalQuizFragment.resetAnimalQuiz();

                    }else if(key.equals(QUIZ_BACKGROUND_COLOR)){

                        myAnimalQuizFragment.modifyBackgroundColor(sharedPreferences);
                        myAnimalQuizFragment.resetAnimalQuiz();
                    }

                    Toast.makeText(MainActivity.this, R.string.change_message, Toast.LENGTH_SHORT).show();

                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


          Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
          startActivity(intent);

        return super.onOptionsItemSelected(item);
    }



}