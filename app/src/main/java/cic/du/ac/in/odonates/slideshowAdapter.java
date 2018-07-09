package cic.du.ac.in.odonates;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class slideshowAdapter extends PagerAdapter{

    private Context context;
    LayoutInflater inflator;
    String Sname;
    String[] l = {"img_1.JPG","img_2.JPG"};

    public slideshowAdapter() {
    }

    public slideshowAdapter(Context context, String Sname)    {
        this.context = context;
        this.Sname = Sname;
    }

    public int[] images = {R.drawable.odonata,R.drawable.odonata};

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
        final ImageView img = view.findViewById(R.id.imageView);
        StorageReference storageReference =  FirebaseStorage.getInstance().getReference().child(Sname+"/"+l[position]);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(img);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("error ", e.getMessage());
            }
        });
        container.addView(view);
        return view;
    }
}
