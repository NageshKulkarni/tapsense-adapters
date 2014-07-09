package com.tapsense.android.admob.adapters.sample;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;
import com.tapsense.android.publisher.TSAdSize;
import com.tapsense.android.publisher.TSAdView;
import com.tapsense.android.publisher.TSAdViewListener;
import com.tapsense.android.publisher.TSErrorCode;
import com.tapsense.android.publisher.TapSenseAds;

public class TapSenseAdMobBannerAdapter implements CustomEventBanner,
    TSAdViewListener {

  CustomEventBannerListener mBannerListener;

  @Override
  public void requestBannerAd(final CustomEventBannerListener listener,
      final Activity activity, String label, String serverParameter,
      AdSize adSize, MediationAdRequest request, Object customEventExtra) {

    try {
      // Remove test mode before going live
      TapSenseAds.setTestMode();
      TapSenseAds.enableDebugLog();
      TapSenseAds.disableGetNextAd(); 

      mBannerListener = listener;

      JSONObject serverParameterJson = new JSONObject(serverParameter);
      TapSenseAds.start(activity, serverParameterJson.getString("pubId"),
          serverParameterJson.getString("appId"),
          serverParameterJson.getString("secretKey"));

      TSAdView bannerView = new TSAdView(activity);
      bannerView.setAdSize(new TSAdSize(adSize.getWidth(), adSize.getHeight()));
      bannerView.setAdUnitId(serverParameterJson.getString("adUnitId"));
      bannerView.setAdViewListener(this);

      // Load the ad in the background
      bannerView.loadAd();
    } catch (JSONException e) {
      listener.onFailedToReceiveAd();
    }
  }

  @Override
  public void onAdViewLoaded(TSAdView adView) {
    mBannerListener.onReceivedAd(adView);
  }

  @Override
  public void onAdViewFailedToLoad(TSAdView adView, TSErrorCode errorCode) {
    mBannerListener.onFailedToReceiveAd();
  }

  @Override
  public void onAdViewExpanded(TSAdView adView) {
    mBannerListener.onPresentScreen();
  }

  @Override
  public void onAdViewCollapsed(TSAdView adView) {
    mBannerListener.onDismissScreen();
  }

  @Override
  public void destroy() {
    // TODO Auto-generated method stub
    
  }

}
