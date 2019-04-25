package com.example.gridview;


enum Color {
    RED, BLACK
}

class Piece {

    Color color;

    Piece (Color c){
        this.color = c;
    }

    public Color getColor(){
        return this.color;
    }

}
