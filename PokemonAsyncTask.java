package edu.lclark.homework2;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by parulsohal on 2/22/16.
 */
public class PokemonAsyncTask extends AsyncTask<String, Void, JSONObject> {

    public static final String TAG = PokemonAsyncTask.class.getSimpleName();

    Pokemon mPokemon;
    PokemonDetailActivity mContext;

    public PokemonAsyncTask(Pokemon pokemon, PokemonDetailActivity pda) {
        mPokemon = pokemon;
        mContext = pda;

    }





    @Override
    protected JSONObject doInBackground(String... params) {


        StringBuilder responseBuilder = new StringBuilder();
        JSONObject jsonObject = null;

        if (params.length == 0) {
            return null;
        }

        String pokemonID = params[0];

        try {
            URL url = new URL("http://pokeapi.co/api/v2/pokemon/" + pokemonID);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStream);
            String line;

            if (isCancelled()) {
                return null;
            }
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
                if (isCancelled()) {
                    return null;
                }
            }

            Log.d(TAG, responseBuilder.toString());
            jsonObject = new JSONObject(responseBuilder.toString());

            if (isCancelled()) {
                return null;
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }


        return jsonObject;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "Started AsyncTask");
        mContext.mPBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCancelled(JSONObject jsonObject) {
        super.onCancelled(jsonObject);
        Log.d(TAG, "AsyncTask cancelled");
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        if (jsonObject == null) {
            Log.e(TAG, "Resulting JSON is null");
        } else {
            try {
                String baseExp = jsonObject.getString("base_experience");
                JSONArray js = new JSONArray(jsonObject.getString("stats"));
                mPokemon.setBaseExp(baseExp);

                for (int i = 0; i < js.length(); i++) {
                    JSONObject stat = js.getJSONObject(i);
                    String baseStat = stat.getString("base_stat");
                    switch (i){
                        case 0:
                            mPokemon.setmSpeed(baseStat);
                            break;
                        case 1:
                            mPokemon.setmSpecDef(baseStat);
                            break;
                        case 2:
                            mPokemon.setmSpecAtk(baseStat);
                            break;
                        case 3:
                            mPokemon.setmDefence(baseStat);
                            break;
                        case 4:
                            mPokemon.setmAttack(baseStat);
                            break;
                        case 5:
                            mPokemon.setmHp(baseStat);

                            break;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mContext.mPBar.setVisibility(View.INVISIBLE);
        mContext.pokemonDetailsAsyncTask();


    }





}
