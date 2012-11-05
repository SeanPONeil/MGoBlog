package com.atami.mgodroid.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

/**
 * A SherlockFragment that displays a WebView.
 * 
 * The WebView is automatically paused or resumed when the Fragment is paused or resumed.
 */
public class SherlockWebViewFragment extends SherlockFragment {
    private PullToRefreshWebView mWebView;
    private boolean mIsWebViewAvailable;

    public SherlockWebViewFragment() {
    }

    /**
     * Called to instantiate the view. Creates and returns the WebView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (mWebView != null) {
            mWebView.getRefreshableView().destroy();
        }
        mWebView = new PullToRefreshWebView(getSherlockActivity());
        mIsWebViewAvailable = true;
        return mWebView;
    }

    /**
     * Called when the fragment is visible to the user and actively running. Resumes the WebView.
     */
    @Override
    public void onPause() {
        super.onPause();
        mWebView.getRefreshableView().onPause();
    }

    /**
     * Called when the fragment is no longer resumed. Pauses the WebView.
     */
    @Override
    public void onResume() {
        mWebView.getRefreshableView().onResume();
        super.onResume();
    }

    /**
     * Called when the WebView has been detached from the fragment.
     * The WebView is no longer available after this time.
     */
    @Override
    public void onDestroyView() {
        mIsWebViewAvailable = false;
        super.onDestroyView();
    }

    /**
     * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
     */
    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.getRefreshableView().destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    /**
     * Gets the WebView.
     */
    public WebView getWebView() {
        return mIsWebViewAvailable ? mWebView.getRefreshableView() : null;
    }
    
    public PullToRefreshWebView getPullToRefreshWebView(){
    	return mIsWebViewAvailable ? mWebView : null;
    }
    
    /**
     * Sets the WebView, if inflating from XML in a child class
     */
    public void setWebView(PullToRefreshWebView wv){
    	mWebView = wv;
    }
}
