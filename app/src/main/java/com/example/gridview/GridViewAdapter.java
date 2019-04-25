package com.example.gridview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private Piece[][] boardOfPieces; // gridview does not use a 2d style. each piece is indexed from 0 to 48 (inclusive)
    private boolean playerOneTurn = true;
    private boolean gameOver = false;
    private Button[] allButtons = new Button[49];
    private TextView titleView;

    public GridViewAdapter(Context c, Piece[][] boardOfPieces) {
        context = c;
        this.boardOfPieces = boardOfPieces;
    }

    public void setTitleView(TextView titleView){
        this.titleView = titleView;
    }

    @Override
    public int getCount() {
        return boardOfPieces.length * boardOfPieces[0].length;
    }

    @Override
    public View getItem(int position) {
      return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Button button;

        if (convertView == null){
            button = new Button(context);
            button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int row = position / 7;
            final int column = position % 7;
            if (boardOfPieces[row][column].color ==  null){
                // means we have not found a color yet. it is null.
                button.setBackground(ContextCompat.getDrawable(context,R.drawable.roundbutton));
            }else if (boardOfPieces[row][column].color.equals(com.example.gridview.Color.RED)){
                button.setBackground(ContextCompat.getDrawable(context,R.drawable.roundbuttonred));
            }else if (boardOfPieces[row][column].color.equals(com.example.gridview.Color.BLACK)){
                button.setBackground(ContextCompat.getDrawable(context,R.drawable.roundbuttonblack));
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean addedIn = addPiece(column, button);
                    if (addedIn){
                        if (isGameOver()){
                            if(playerOneTurn){
                                // player 1 won
                                Toast.makeText(context, "Player 1 won!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Player 2 won!", Toast.LENGTH_SHORT).show();
                            }
                            reset();
                        }
                        playerOneTurn = !playerOneTurn;
                        titleView.setText(playerOneTurn ? "Player One's Turn": "Player Two's Turn");
                        titleView.setTextColor(playerOneTurn ? Color.BLACK : Color.RED);
                    }else{
                        Toast.makeText(context, "This column is full; please pick another", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else {
            button = (Button) convertView;
        }
        button.setBackground(ContextCompat.getDrawable(context,R.drawable.roundbutton));
        allButtons[position] = button;
        return button;
    }


    // addPiece method
    private boolean addPiece(int col, Button button) {

        for (int i = 6; i >= 0; i--) {
            if (boardOfPieces[i][col].color == null) {
                if (playerOneTurn) {
                    boardOfPieces[i][col] = new Piece(com.example.gridview.Color.BLACK);
                    for (int j = 0; j < i; j++) {
                       animateColor(allButtons[j * 7 + col], "black");
                    }try{
                        Thread.sleep(100);
                        allButtons[i * 7 + col].setBackground(ContextCompat.getDrawable(context,R.drawable.roundbuttonblack));
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                } else {
                    boardOfPieces[i][col] = new Piece(com.example.gridview.Color.RED);
                    for (int j = 0; j < i; j++) {
                        animateColor(allButtons[j * 7 + col], "red");
                    }
                    try{
                        Thread.sleep(100);
                        allButtons[i * 7 + col].setBackground(ContextCompat.getDrawable(context,R.drawable.roundbuttonred));
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                break;
            } else { // this piece has been added.
                if (i == 0){
                    return false;
                }
            }
        }
        return true;
    }
    public void reset() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                boardOfPieces[i][j] = new Piece(null);
            }
        }
        playerOneTurn = true;
        gameOver = false;
        for (int x = 0; x < 49; x++) {
            allButtons[x].setBackground(ContextCompat.getDrawable(context,R.drawable.roundbutton));
        }
    }

    public boolean isGameOver() {
        //check rows
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 4; c++){
                if (boardOfPieces[r][c].color != null &&
                        boardOfPieces[r][c].color == boardOfPieces[r][c + 1].color &&
                        boardOfPieces[r][c].color == boardOfPieces[r][c + 2].color &&
                        boardOfPieces[r][c].color == boardOfPieces[r][c + 3].color) {
                    return true;
                }
            }
        }
        //check cols
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 7; c++){
                if (boardOfPieces[r][c].color != null &&
                        boardOfPieces[r][c].color == boardOfPieces[r + 1][c].color &&
                        boardOfPieces[r][c].color == boardOfPieces[r + 2][c].color &&
                        boardOfPieces[r][c].color == boardOfPieces[r + 3][c].color) {
                    return true;
                }
            }
        }
        // check top left to btm right diagonals
        for (int r = 0; r < 4; r++){
            for(int c = 0; c < 4; c++){
                if (boardOfPieces[r][c].color != null &&
                        boardOfPieces[r][c].color == boardOfPieces[r + 1][c + 1].color &&
                        boardOfPieces[r][c].color == boardOfPieces[r + 2][c + 2].color &&
                        boardOfPieces[r][c].color == boardOfPieces[r + 3][c + 3].color){
                    return true;
                }
            }
        }
        // check bottom left to top right diagonals
        for (int r = 0; r < 4; r++){
            for (int c = 3; c < 7; c++){
                if(boardOfPieces[r][c].color != null &&
                        boardOfPieces[r][c].color == boardOfPieces[r + 1][c - 1].color &&
                        boardOfPieces[r][c].color == boardOfPieces[r + 2][c - 2].color &&
                        boardOfPieces[r][c].color == boardOfPieces[r + 3][c - 3].color){
                    return true;
                }
            }
        }
        return false;
    }

    private void animateColor(final Button button, String color){
        final boolean isRed = color.equals("red");
        //int colorTo = context.getResources().getColor(R.color.colorGreen);
        //int colorFrom = context.getResources().getColor(isRed?  R.color.coloRed : R.color.colorBlack);
        button.setBackgroundResource(isRed ? R.drawable.colorchangered : R.drawable.colorchangeblack);
        AnimationDrawable buttonBackground = (AnimationDrawable) button.getBackground();
        buttonBackground.start();
        /*ValueAnimator animator = ObjectAnimator.ofInt(button, "backgroundColor", isRed ? Color.RED: Color.BLACK ,Color.GREEN);
        animator.setDuration(50);
        animator.setRepeatCount(1);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d("animate","animate starts");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
               // button.setBackground(ContextCompat.getDrawable(context,R.drawable.roundbutton));
                Log.d("animate","animate ends");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();*/
    }

}