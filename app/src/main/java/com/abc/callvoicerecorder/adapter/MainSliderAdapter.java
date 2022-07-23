package com.abc.callvoicerecorder.adapter;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

import static com.abc.callvoicerecorder.constant.Constants.Image1;
import static com.abc.callvoicerecorder.constant.Constants.Image2;
import static com.abc.callvoicerecorder.constant.Constants.Image3;


public class MainSliderAdapter extends SliderAdapter {

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide(Image1);
                break;
            case 1:
                viewHolder.bindImageSlide(Image2);
                break;
            case 2:
                viewHolder.bindImageSlide(Image3);
                break;
        }
    }

}