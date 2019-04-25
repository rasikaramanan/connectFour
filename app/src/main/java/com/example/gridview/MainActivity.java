package com.example.gridview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {
    TextView currentPlayer;
    Piece[][] pieces = new Piece[7][7];
    GridViewAdapter gridViewAdapter;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentPlayer = findViewById(R.id.currPlayer);
        currentPlayer.setText("Player One's Turn");
        for (int row = 0; row < 7; row++){
            for (int column = 0; column < 7; column++){
                pieces[row][column] = new Piece(null);
            }
        }
        gridView = (GridView) findViewById(R.id.gridView);
        gridViewAdapter = new GridViewAdapter(getApplicationContext(), pieces );
        gridViewAdapter.setTitleView(currentPlayer);
        gridView.setAdapter(gridViewAdapter);
    }

    public void reset(View view) {
        gridViewAdapter.reset();
        gridViewAdapter.notifyDataSetChanged();
        /*Button button = findViewById(R.id.buttonReset);
        button.setBackgroundResource(R.drawable.roundbutton);*/
    }

}
