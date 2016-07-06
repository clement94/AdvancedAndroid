package com.cberthelot.advancedandoid.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cberthelot.advancedandoid.R;

/**
 * Created by cberthelot on 04/07/2016.
 */
public class DefaultFragment extends Fragment {
    private static final String ARTICLE_ID = "ARTICLE_ID";

    private final static String TAG = "DynFragment1";

    public final static String AGE = "com.cberthelot.advancedandoid.AGE";

    private Button mCall = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "Create view for default fragment : " +  this.getArguments().getInt(ARTICLE_ID));

        View view = inflater.inflate(R.layout.default_fragment, container, false);

        mCall = (Button) view.findViewById(R.id.myCall);
        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On doit ouvrir le composeur téléphonique
                Uri tel = Uri.parse("tel:0612956789");
                Intent intent = new Intent(Intent.ACTION_DIAL, tel);
                startActivity(intent);
            }
        });

        return view;
    }

    public static DefaultFragment newInstance(int articleId) {
        DefaultFragment fragment = new DefaultFragment();
        Bundle args = new Bundle();
        args.putInt(ARTICLE_ID, articleId);
        fragment.setArguments(args);
        return fragment;
    }
}
