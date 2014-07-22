package com.tapsense.adapters;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;
import com.tapsense.android.publisher.TSErrorCode;
import com.tapsense.android.publisher.TapSenseAdView;
import com.tapsense.android.publisher.TapSenseAdViewListener;
import com.tapsense.android.publisher.TapSenseAds;

public class TapSenseAdMobBannerAdapter implements CustomEventBanner,
    TapSenseAdViewListener {

  CustomEventBannerListener mBannerListener;

  @Override
  public void requestBannerAd(final CustomEventBannerListener listener,
      final Activity activity, String label, String serverParameter,
      AdSize adSize, MediationAdRequest request, Object customEventExtra) {

    try {
      // Remove test mode before going live
      TapSenseAds.setTestMode();

      mBannerListener = listener;

      JSONObject serverParameterJson = new JSONObject(serverParameter);

      TapSenseAdView bannerView = new TapSenseAdView(activity);
      bannerView.setAdUnitId(serverParameterJson.getString("adUnitId"));
      bannerView.setAdViewListener(this);

      // Load the ad in the background
      bannerView.loadAd();
    } catch (JSONException e) {
      listener.onFailedToReceiveAd();
    }
  }

  @Override
  public void onAdViewLoaded(TapSenseAdView adView) {
    mBannerListener.onReceivedAd(adView);
  }

  @Override
  public void onAdViewFailedToLoad(TapSenseAdView adView, TSErrorCode errorCode) {
    mBannerListener.onFailedToReceiveAd();
  }

  @Override
  public void onAdViewExpanded(TapSenseAdView adView) {
    mBannerListener.onPresentScreen();
  }

  @Override
  public void onAdViewCollapsed(TapSenseAdView adView) {
    mBannerListener.onDismissScreen();
  }

  @Override
  public void destroy() {
    // TODO Auto-generated method stub

  }

}
