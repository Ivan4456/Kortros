package ru.mysmartflat.kortros;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.Timer;

public class BilderFragment extends Fragment implements View.OnTouchListener {

    private OnFragmentInteractionListener mListener;
    ViewPager viewPager;
    Timer timer;

    private VideoView videoView;
    private MediaController mediaController;
    private Button button_Play;
    private ImageView imageView_video;

/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "!!! must implement OnFragmentInteractionListener");
        }
    }
*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception
        /*
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "!! must implement OnHeadlineSelectedListener");
        }
    */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        timer.cancel();
    }

    interface Listener {
        public void onTownClickEvent(Integer Pos);
        public void onFilterClickEvent();
    }

    public BilderFragment() {
        // Required empty public constructor
    }

    public static BilderFragment newInstance() {
        BilderFragment fragment = new BilderFragment();
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onStop() {
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bilder, container, false);

        final MapView mapview = (MapView) view.findViewById(R.id.mapview_Bilder_ID);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager_ID);
        viewPager.setClipToPadding(false);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPadding(20, 0, 20, 0);

        ViewPager_Bilder_Adapter viewPager_Bilder_Adapter = new ViewPager_Bilder_Adapter(getActivity());
        viewPager.setAdapter(viewPager_Bilder_Adapter);

        viewPager.setOnTouchListener(this);

        timer = new Timer();
        timer.scheduleAtFixedRate(new BilderFragment.TimerTask(),2000,4000);

        mapview.getMap().move(
                new CameraPosition(new com.yandex.mapkit.geometry.Point(55.75222, 37.61556), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        mapview.getMap().getMapObjects().addPlacemark(new com.yandex.mapkit.geometry.Point(55.73222, 37.61556), ImageProvider.fromResource(view.getContext(),R.drawable.map_marker));

        final ScrollView mainScrollView = (ScrollView) view.findViewById(R.id.ScrollView_ID);

        ImageView transparentImageView = (ImageView) view.findViewById(R.id.transparent_image);

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        imageView_video = (ImageView) view.findViewById(R.id.imageView_video_ID);
        //imageView_video.setFitToScreen(true);
        //imageView_video.setScaleType(ImageView.ScaleType.FIT_CENTER);

        videoView = (VideoView) view.findViewById(R.id.videoView_ID);
        mediaController = new MediaController(view.getContext());

        Uri uri = Uri.parse("android.resource://ru.mysmartflat.kortros/"+R.raw.kib);
        videoView.setVideoURI(uri);

        videoView.setMediaController(mediaController);

        videoView.setZOrderMediaOverlay(true);

        imageView_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageView_video.setVisibility(View.INVISIBLE);
                videoView.start();

            }
        });

        mainScrollView.smoothScrollTo(0,0);

        return view;
    }

    public class TimerTask extends java.util.TimerTask{
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int pos = viewPager.getCurrentItem();
                    pos++;
                    if (pos > 3) {
                        pos = 0;
                    }
                    viewPager.setCurrentItem(pos);
                }
            });
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                break;

            case MotionEvent.ACTION_POINTER_UP:
                break;

            case MotionEvent.ACTION_MOVE:

                timer.cancel();
                timer = new Timer();
                timer.scheduleAtFixedRate(new BilderFragment.TimerTask(),10000,4000);

                break;
        }
        return false;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
}
