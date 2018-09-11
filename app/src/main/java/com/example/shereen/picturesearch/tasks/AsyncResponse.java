package com.example.shereen.picturesearch.tasks;

import com.example.shereen.picturesearch.entity.SingleImage;

import java.util.List;

public interface AsyncResponse {
    void processFinish(List<SingleImage> imageUrls);
}
