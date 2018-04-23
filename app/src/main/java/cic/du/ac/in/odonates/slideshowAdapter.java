package cic.du.ac.in.odonates;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class slideshowAdapter extends PagerAdapter{

    private Context context;
    LayoutInflater inflator;

    public slideshowAdapter() {
    }

    public slideshowAdapter(Context context)    {
        this.context = context;
    }

    public int[] images = {R.drawable.img1,R.drawable.img2,R.drawable.img3};

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.slideshow,container,false);
        ImageView img = view.findViewById(R.id.imageView);
        img.setImageResource(images[position]);
        container.addView(view);
        return view;
    }
}
