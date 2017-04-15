package com.jadebyte.popularmovies.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jadebyte.popularmovies.R;

import java.lang.ref.WeakReference;

public class MyGlide{
    public static void load(Context context, ImageView view, String url, final ProgressBar progressBar) {
        
        // Using WeakReferences to avoid leaking Context and View objects
        final Context weakCxt = new WeakReference<>(context).get();
        final ImageView weakView = new WeakReference<>(view).get();
        final ProgressBar weakProg = new WeakReference<>(progressBar).get();

        Glide.with(weakCxt)
                .load(url)
                .asBitmap()
                .error(R.drawable.ic_default_movie_poster)
                .placeholder(R.drawable.ic_default_movie_poster)
                .skipMemoryCache(true) // Just to be on the save path, don't cache images in memory since many images will be loaded in RecyclerView
                                        // This is to avoid the popular plague: OutOfMemoryException. Disk cache should be enough.
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        if (weakProg != null) {
                            int space =(int) MyConvert.dpToPx(10, weakCxt);
                            weakProg.setVisibility(View.GONE);
                            weakView.setPadding(space, space, space, space);
                            weakView.setScaleType(ImageView.ScaleType.CENTER);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (weakProg != null) {
                            weakProg.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .into(weakView);
    }
}
