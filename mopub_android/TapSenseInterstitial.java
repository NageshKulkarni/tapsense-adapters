package com.mopub.mobileads;

import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.tapsense.android.publisher.TSErrorCode;
import com.tapsense.android.publisher.TapSenseAds;
import com.tapsense.android.publisher.TapSenseAdsListener;

public class TapSenseInterstitial extends CustomEventInterstitial implements
    TapSenseAdsListener {

  private CustomEventInterstitialListener mInterstitialListener;

  @Override
  protected void loadInterstitial(Context context,
      CustomEventInterstitialListener customEventInterstitialListener,
      Map<String, Object> localExtras, Map<String, String> serverExtras) {
    mInterstitialListener = customEventInterstitialListener;

    if (!(context instanceof Activity)) {
      mInterstitialListener
          .onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
      return;
    }

    String pubId = serverExtras.containsKey("pubId") ? serverExtras
        .get("pubId") : "";
    String appId = serverExtras.containsKey("appId") ? serverExtras
        .get("appId") : "";
    String secretKey = serverExtras.containsKey("secretKey") ? serverExtras
        .get("secretKey") : "";
    String adUnitId = serverExtras.containsKey("adUnitId") ? serverExtras
        .get("adUnitId") : "";
    boolean videoOnly = serverExtras.containsKey("videoOnly") ? Boolean
        .valueOf(serverExtras.get("videoOnly")) : false;
    boolean adFlowOnly = serverExtras.containsKey("adFlowOnly") ? Boolean
        .valueOf(serverExtras.get("adFlowOnly")) : false;

    TapSenseAds.disableGetNextAd();
    TapSenseAds.enableVideoOnly(videoOnly);
    TapSenseAds.enableAdFlowOnly(adFlowOnly);
    TapSenseAds.setInterstitialAdUnitId(adUnitId);

    // Remove this before submitting to Play Store
    TapSenseAds.setTestMode();

    TapSenseAds.start(context, pubId, appId, secretKey);
    TapSenseAds.getInstance().setListener(this);
    TapSenseAds.getInstance().requestAd();
  }

  @Override
  protected void showInterstitial() {
    TapSenseAds.getInstance().showAd();
  }

  @Override
  protected void onInvalidate() {
    if (TapSenseAds.getInstance() != null) {
      TapSenseAds.getInstance().setListener(null);
    }
  }

  @Override
  public void onTapSenseAdDismissed() {
    if (mInterstitialListener != null) {
      mInterstitialListener.onInterstitialDismissed();
    }
  }

  @Override
  public void onTapSenseAdFailedToLoad(TSErrorCode errorCode) {
    if (mInterstitialListener != null) {
      mInterstitialListener
          .onInterstitialFailed(MoPubErrorCode.NETWORK_NO_FILL);
    }
  }

  @Override
  public void onTapSenseAdLoaded() {
    if (mInterstitialListener != null) {
      mInterstitialListener.onInterstitialLoaded();
    }
  }

  @Override
  public void onTapSenseAdShown() {
    if (mInterstitialListener != null) {
      mInterstitialListener.onInterstitialShown();
    }
  }
}
