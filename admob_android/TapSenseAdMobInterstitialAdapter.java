package com.tapsense.android.admob.adapters.sample;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;
import com.tapsense.android.publisher.TSErrorCode;
import com.tapsense.android.publisher.TapSenseAds;
import com.tapsense.android.publisher.TapSenseAdsListener;

public class TapSenseAdMobInterstitialAdapter implements
    CustomEventInterstitial, TapSenseAdsListener {

  CustomEventInterstitialListener mInterstitialListener;

  @Override
  public void requestInterstitialAd(
      final CustomEventInterstitialListener listener, final Activity activity,
      String label, String serverParameter,
      MediationAdRequest mediationAdRequest, Object extra) {
    try {
      // Remove test mode before going live
      TapSenseAds.setTestMode();
      TapSenseAds.enableDebugLog();
      TapSenseAds.disableGetNextAd();

      mInterstitialListener = listener;

      JSONObject serverParameterJson = new JSONObject(serverParameter);
      TapSenseAds.setInterstitialAdUnitId(serverParameterJson.getString("adUnitId"));
      TapSenseAds.start(activity, serverParameterJson.getString("pubId"),
          serverParameterJson.getString("appId"),
          serverParameterJson.getString("secretKey"));
      TapSenseAds.getInstance().setListener(this);
      TapSenseAds.getInstance().requestAd();
    } catch (JSONException e) {
      listener.onFailedToReceiveAd();
    }
  }

  @Override
  public void showInterstitial() {
    if (TapSenseAds.getInstance().isReady()) {
      TapSenseAds.getInstance().showAd();
    }
  }

  @Override
  public void destroy() {
    // TODO Auto-generated method stub

  }

  // ===========================================================================
  // TapSenseAdsListener methods
  // ===========================================================================

  @Override
  public void onTapSenseAdLoaded() {
    mInterstitialListener.onReceivedAd();

  }

  @Override
  public void onTapSenseAdFailedToLoad(TSErrorCode errorCode) {
    mInterstitialListener.onFailedToReceiveAd();

  }

  @Override
  public void onTapSenseAdShown() {
    mInterstitialListener.onPresentScreen();

  }

  @Override
  public void onTapSenseAdDismissed() {
    mInterstitialListener.onDismissScreen();

  }

}
