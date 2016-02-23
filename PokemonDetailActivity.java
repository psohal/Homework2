package edu.lclark.homework2;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by parulsohal on 2/22/16.
 */
public class PokemonDetailActivity extends AppCompatActivity {
    public static final String ARG_Pokemon = "POKEMON";
    private static final String TAG = PokemonDetailActivity.class.getSimpleName();
    TextView mId, mWeight, mHeight, mHp, mExp, mAttack, mDefense, mSpeed, mSpecAttack, mSpecDefence, mName;
    ImageView mPokemonImage;
    ProgressBar mPBar;
    Pokemon mPokemon;
    PokemonAsyncTask mAsyncTask;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPokemon = (Pokemon) getIntent().getSerializableExtra(ARG_Pokemon);
        setContentView(R.layout.activity_pokemon_detail);

        mExp = (TextView) findViewById(R.id.detail_pokemon_base_exp);
        mHp = (TextView) findViewById(R.id.detail_pokemon_hp);
        mAttack = (TextView) findViewById(R.id.detail_pokemon_attack);
        mDefense = (TextView) findViewById(R.id.detail_pokemon_defense);
        mSpecDefence = (TextView) findViewById(R.id.detail_pokemon_spc_def);
        mSpecAttack = (TextView) findViewById(R.id.detail_pokemon_spc_att);
        mSpeed = (TextView) findViewById(R.id.detail_pokemon_speed);


        mId = (TextView) findViewById(R.id.detail_pokemon_id);
        mName = (TextView) findViewById(R.id.detail_pokemon_name);
        mHeight = (TextView) findViewById(R.id.detail_pokemon_height);
        mWeight = (TextView) findViewById(R.id.detail_pokemon_weight);
        mPokemonImage = (ImageView) findViewById(R.id.detail_pokemon_image);

        Picasso.with(this).load(mPokemon.getImageUrl()).fit().centerInside().into(mPokemonImage);

        mId.setText(mPokemon.getId());
        mName.setText(mPokemon.getName());
        mHeight.setText(getString(R.string.height_label, mPokemon.getHeight()));
        mWeight.setText(getString(R.string.weight_label, mPokemon.getWeight()));

        mPBar = (ProgressBar) findViewById(R.id.detail_pokemon_progress);
        mPBar.setIndeterminate(true);
        mPBar.setVisibility(View.GONE);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (!mPokemon.isChanged()) {
            if (networkInfo == null || !networkInfo.isConnected()) {
                mExp.setText(R.string.Error_internet);
            } else {
                mAsyncTask = new PokemonAsyncTask(mPokemon, this);
                mAsyncTask.execute(mPokemon.getId());
            }
        } else {
            pokemonDetailsAsyncTask();
        }
    }


    public void pokemonDetailsAsyncTask() {
        //Log.d(TAG, "exp = " + mPokemon.getBaseExp());
        String hp = getString(R.string.pokemon_hp, mPokemon.getmHp());
        mHp.setText(hp);

        String exp = getString(R.string.base_exp, mPokemon.getBaseExp());
        mExp.setText(exp);

        String attack = getString(R.string.attack, mPokemon.getmAttack());
        mAttack.setText(attack);

        String defense = getString(R.string.defence, mPokemon.getmDefence());
        mDefense.setText(defense);

        String speed = getString(R.string.speed, mPokemon.getmSpeed());
        mSpeed.setText(speed);

        String specattack = getString(R.string.specialattack, mPokemon.getmHp());
        mSpecAttack.setText(specattack);

        String specdefence = getString(R.string.specialdefense, mPokemon.getmSpecDef());
        mSpecDefence.setText(specdefence);
    }

    @Override
    public void onBackPressed() {
        endActivity();
    }

    public void endActivity() {
        if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
            mAsyncTask.cancel(true);
        }

        Intent data = new Intent();
        data.putExtra(ARG_Pokemon, mPokemon);
        setResult(Activity.RESULT_OK, data);
        finish();
    }


}
