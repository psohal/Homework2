package edu.lclark.homework2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements PokemonRecyclerViewAdapter.OnPokemonRowClickListener {

    private static final int CODE_POKEMON = 0;
    RecyclerView mRecyclerView;
    Pokedex mPokemon;
    PokemonRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_activity_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPokemon = new Pokedex();
        mAdapter = new PokemonRecyclerViewAdapter(mPokemon.getPokemons(), this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void onPokemonRowClick(Pokemon pokemon) {
        Intent intent = new Intent(MainActivity.this, PokemonDetailActivity.class);
        intent.putExtra(PokemonDetailActivity.ARG_Pokemon, pokemon);
        startActivityForResult(intent, CODE_POKEMON);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK  && requestCode == CODE_POKEMON){
            Pokemon pokemon = (Pokemon) data.getSerializableExtra(PokemonDetailActivity.ARG_Pokemon);
            if(mPokemon != null) {
                mPokemon.setPokemon(Integer.parseInt(pokemon.getId()), pokemon);
                mAdapter.notifyItemChanged(Integer.parseInt(pokemon.getId()) - 1);
            }
        }
    }

}
