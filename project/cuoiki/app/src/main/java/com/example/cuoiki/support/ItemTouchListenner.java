package com.example.cuoiki.support;

public interface ItemTouchListenner {
    void onMove(int oldPosition, int newPosition);

    void swipe(int position, int direction);
}
