package com.mocom.com.mdancingproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Activities.ShowPictureActivity;
import com.mocom.com.mdancingproject.Dao.ImageBannerDao;
import com.mocom.com.mdancingproject.R;

import java.util.List;

public class ImageBannerAdapter extends PagerAdapter {

    private String[] urls;
    private LayoutInflater inflater;
    private Context context;
    private List<ImageBannerDao> imageBannerDaoList;

    public ImageBannerAdapter(Context context, String[] urls,List<ImageBannerDao> imageBannerDaoList) {
        this.urls = urls;
        this.context = context;
        this.imageBannerDaoList = imageBannerDaoList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slide_banner, view, false);
        ImageBannerDao imageBannerDao = imageBannerDaoList.get(position);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.image);

        Glide.with(context)
                .load(urls[position])
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,imageBannerDaoList.get(position).getDesc()+"/"+urls[position] , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ShowPictureActivity.class);
                intent.putExtra("imageUrl", urls[position]);
                intent.putExtra("txtDesc", imageBannerDao.getDesc());
                context.startActivity(intent);
            }
        });

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
