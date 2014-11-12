package com.coloc.tizoom;


public interface ZoomableView {
    public void reset();
    public void setZoomFactor(float factor);
    public void setOffset(int x, int y);
}
