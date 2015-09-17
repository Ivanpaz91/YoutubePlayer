package com.tirdis.watchorread.view;





import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.tirdis.watchorread.ApplicationController;
import com.tirdis.watchorread.ArticleViewActivity;
import com.tirdis.watchorread.R;
import com.tirdis.watchorread.model.Content;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.droidsonroids.gif.GifImageView;

import com.google.android.youtube.player.YouTubePlayerView;

import org.parceler.Parcels;

import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;


public class ArticleFragment extends Fragment {


    private   int mIndex;
    private  int mMarginTop,mMarginBottom;

    private Content mContent;


    @InjectView(R.id.webView)
    VideoEnabledWebView  mYouTubeView;

    @InjectView(R.id.txt_title_article)
    TextView mTitleTextView;

    @InjectView(R.id.txt_content_article)
    TextView mContentTextView;
    @InjectView(R.id.txt_index_article)
    TextView mIndexTextView;

    @InjectView(R.id.btn_share_content)
    ImageView mShareImageView;

    @InjectView(R.id.videoLayout)
    ViewGroup  videoLayout ;

    @InjectView(R.id.nonVideoLayout)
    View nonVideoLayout;
    @InjectView(R.id.progressBar_video)
    ProgressBar mProgressBar;

    private VideoEnabledWebChromeClient webChromeClient;
    View loadingView;
    String playVideo;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
//        if(isVisibleToUser){
//            //     mYouTubeView.setVisibility(View.INVISIBLE);
//            if(mYouTubeView!=null){
//
//            }
//            //mYouTubeView.setBackgroundColor(new Color().BLACK);
//        }
//        else{
//            if(mYouTubeView!=null){
//                mYouTubeView.onPause();
//            }
//        }
    }

    public static ArticleFragment newInstance(Content content,int start) {
        ArticleFragment fragment = new ArticleFragment();

        Bundle args = new Bundle();

        Parcelable wrappedContent = Parcels.wrap(content);
        args.putInt("index", start);
        args.putParcelable("content", wrappedContent);

        fragment.setArguments(args);


        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt("index");
            Parcelable parcelable = getArguments().getParcelable("content");
            mContent = Parcels.unwrap(parcelable);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
//        try {
//            ((ArticleViewActivity) getActivity()).resetVideo();
//        }catch (Exception e){};
        if(mContent.getId().equals("0")){
            view  = inflater.inflate(R.layout.fragment_article_empty, container, false);
            showContentEmptyArticle(view);

        }else{
            view = inflater.inflate(R.layout.fragment_article, container, false);
            ButterKnife.inject(this,view);

            loadingView =inflater.inflate(R.layout.view_loading_video, null);
          //   showContent();
            if(((ArticleViewActivity)getActivity()).isFirstTime){
                showContent();
                ((ArticleViewActivity)getActivity()).isFirstTime = false;
            }

        }

        // Inflate the layout for this fragment




        return view;

    }
    public void refresh(){
        if(!mContent.getId().equals("0")){

            showContent();}

    }
    private void setHeaderTitle(String text) {


    }

    private void showContentEmptyArticle(View view) {
        TextView textView = (TextView)view.findViewById(R.id.txt_title_article_empty);
        textView.setText(mContent.getText().toUpperCase() + " "+ textView.getText().toString());

    }


    private void showContent() {
        if(mContent!=null){

            Runnable mRunnable;
            Handler mHandler = new Handler();

            mRunnable = new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mYouTubeView.setVisibility(View.VISIBLE);
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
       //     mYouTubeView.setVisibility(View.INVISIBLE);

            setWebViewSetting();
          //  mYouTubeView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mYouTubeView.setBackgroundColor(new Color().BLACK);
            mYouTubeView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mYouTubeView.getSettings().setJavaScriptEnabled(true);
//            mYouTubeView.setVisibility(View.INVISIBLE);

            mTitleTextView.setText(mContent.getHead());
            mContentTextView.setText(mContent.getText());
            mProgressBar.setVisibility(View.VISIBLE);



            mIndexTextView.setText(mContent.getId() + "/" + mContent.getLang_id());
            mShareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    Uri uri = Uri.parse(mContent.getYtlink());
                  //  shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,mContent.getYtlink());
                    startActivity(shareIntent);
                    mYouTubeView.onPause();
                }
            });

        }else{
            mTitleTextView.setText("Empty");
            mContentTextView.setText("Empty");
        }

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
     //   onPauseVideo();
    }

    private void setWebViewSetting() {
        webChromeClient = new VideoEnabledWebChromeClient((View)nonVideoLayout, videoLayout, loadingView, mYouTubeView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                // Your code...
            }


        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
        {
            @Override
            public void toggledFullscreen(boolean fullscreen)
            {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen)
                {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
                        //noinspection all
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }

                    View viewPagerView = (View)nonVideoLayout.getParent().getParent();
                    ViewGroup.MarginLayoutParams lp = ( ViewGroup.MarginLayoutParams)viewPagerView.getLayoutParams();
                    mMarginTop = lp.topMargin;
                    mMarginBottom = lp.bottomMargin;
                    lp.topMargin = 0;
                    lp.bottomMargin = 0;
                    try {
                        View headerView = ((View) viewPagerView.getParent()).findViewById(R.id.view_header);
                        if (headerView != null) {
                            headerView.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        Log.e("Header View", "error");
                    }
                    try {
                        View footerView = ((View) viewPagerView.getParent()).findViewById(R.id.view_footer);
                        if (footerView != null) {
                            footerView.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        Log.e("Footer View", "error");
                    }
                    ((ArticleViewActivity)getActivity()).swipeOff();
                 getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
                else
                {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
                        //noinspection all
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }

                    View viewPagerView = (View)nonVideoLayout.getParent().getParent();
                    ViewGroup.MarginLayoutParams lp = ( ViewGroup.MarginLayoutParams)viewPagerView.getLayoutParams();

                    lp.topMargin = mMarginTop;
                    lp.bottomMargin = mMarginBottom;
                    try {
                        View headerView = ((View) viewPagerView.getParent()).findViewById(R.id.view_header);
                        if (headerView != null) {
                            headerView.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                        Log.e("Header View", "error");
                    }
                    try {
                        View footerView = ((View) viewPagerView.getParent()).findViewById(R.id.view_footer);
                        if (footerView != null) {
                            footerView.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                        Log.e("Footer View", "error");
                    }
                    ((ArticleViewActivity)getActivity()).swipeOn();
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

            }
        });
        //youtube view setting
         playVideo = convertToIframe(mContent.getYtlink());

        mYouTubeView.setWebChromeClient(webChromeClient);
        mYouTubeView.loadUrl(playVideo);
//            mYouTubeView.setOnTouchListener(new View.OnTouchListener() {
//
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return (event.getAction() == MotionEvent.ACTION_MOVE);
//                }
//            });

   //     mYouTubeView.loadData(playVideo,"text/html", "utf-8");

    }

    private String convertToIframe(String link) {


        String youtubeId = link.substring(link.lastIndexOf("=") + 1);
        youtubeId = "http://www.youtube.com/embed/" + youtubeId;
        return youtubeId;
     //   String convertedId =  "http://www.youtube.com/embed/" + youtubeId + "?rel=0";

     //   String youtubeId  ="i7crnjc6Rb4";
//        String html =
//                "<iframe class=\"youtube-player\" "
//                        + "style=\"border: 0; width: 110%; height: 105%;"
//                        + "padding:0px; margin:0px\" "
//                        + "id=\"ytplayer\" type=\"text/html\" "
//                        + "src=\"http://www.youtube.com/embed/" + youtubeId +"?playlist="+ youtubeId
//                        + "?fs=0\" frameborder=\"0\" " + "allowfullscreen autobuffer "
//                        + "controls onclick=\"this.play()\">\n" + "</iframe>\n";
////        String html =
////                "<iframe class=\"youtube-player\" "
////                        + "style=\"border: 0; width: 110%; height: 105%;"
////                        + "padding:0px; margin:0px\" "
////                        + "id=\"ytplayer\" type=\"text/html\" "
////                        + "src=\"https://www.youtube.com/v/" + youtubeId +"?playlist="+ youtubeId
////                        + "?fs=0\" frameborder=\"0\" " + "allowfullscreen autobuffer "
////                        + "controls onclick=\"this.play()\">\n" + "</iframe>\n";
//
//        return html;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            mYouTubeView.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onPause() {
        super.onPause();

//        if(mYouTubeView!=null){
//            mYouTubeView.onPause();
//            mYouTubeView.clearHistory();
//            mYouTubeView.loadUrl("about:blank");
//            mYouTubeView = null;
//        }

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mYouTubeView.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPauseVideo(){
        try{
            mYouTubeView.onPause();
            mYouTubeView.clearHistory();
            mYouTubeView.loadUrl("about:blank");
        //   mYouTubeView = null;

        }catch(Exception e){}
    }
    public void onStartVideo(){
        mYouTubeView.loadUrl(playVideo);
    }


}
