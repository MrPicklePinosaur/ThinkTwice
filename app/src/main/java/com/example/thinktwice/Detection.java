package com.example.thinktwice;

import android.graphics.Bitmap;
import android.provider.ContactsContract;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.WebDetection;
import com.google.protobuf.ByteString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Detection {

    public static String labelGuesser(Bitmap bmp) {
        String result = null;
        ArrayList<AnnotateImageRequest> requests = new ArrayList<AnnotateImageRequest>();

        //Create an image from the android camera picture
        ByteString img_bytes = Detection.bitmapToByteSting(bmp);
        Image img = Image.newBuilder().setContent(img_bytes).build();

        //setup request
        Feature feature = Feature.newBuilder().setType(Feature.Type.WEB_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(img).build();
        requests.add(request);


        try {
            ImageAnnotatorClient client = ImageAnnotatorClient.create();
            List<AnnotateImageResponse> response = client.batchAnnotateImages(requests).getResponsesList();

            for (AnnotateImageResponse resp : response) {
                /*
                if (resp.hasError()) {
                    System.out.println("Error in response: "+resp.getError().getMessage()); return;
                } */

                WebDetection annotation = resp.getWebDetection();

                for (WebDetection.WebLabel label : annotation.getBestGuessLabelsList()) {
                    result = label.getLabel();
                }
            }
               
        } catch(IOException ex) { System.out.println("Error initing ImgAnnotateClient: "+ex); }

        assert (result != null): "No result found";
        return result;

    }

    //Take pictures taken by android camera and converts them to images taht are compatable with google images
    //helped a lot: https://stackoverflow.com/questions/4989182/converting-java-bitmap-to-byte-array
    public static ByteString bitmapToByteSting(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bytes = stream.toByteArray();

        return ByteString.copyFrom(bytes);
    }
}
