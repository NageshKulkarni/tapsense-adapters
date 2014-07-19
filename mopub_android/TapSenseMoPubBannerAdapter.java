package com.mopub.mobileads;

import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.tapsense.android.publisher.TSErrorCode;
import com.tapsense.android.publisher.TapSenseAdView;
import com.tapsense.android.publisher.TapSenseAdViewListener;
import com.tapsense.android.publisher.TapSenseAds;

public class TapSenseMoPubBannerAdapter extends CustomEventBanner implements
    TapSenseAdViewListener {

  private CustomEventBannerListener mBannerListener;

  protected void loadBanner(Context context,
      CustomEventBannerListener customEventBannerListener,
      Map<String, Object> localExtras, Map<String, String> serverExtras) {

    mBannerListener = customEventBannerListener;

    if (!(context instanceof Activity)) {
      mBannerListener
          .onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
      return;
    }

    String adUnitId = serverExtras.containsKey("adUnitId") ? serverExtras
        .get("adUnitId") : "";

    // Remove this before submitting to Play Store
    TapSenseAds.setTestMode();
    TapSenseAds.setShowDebugLog();

    TapSenseAdView bannerView = new TapSenseAdView(context);

    bannerView = new TapSenseAdView(context);
    bannerView.setAdUnitId(adUnitId);
    bannerView.setAdViewListener(this);
    bannerView.loadAd();
  }

  @Override
  protected void onInvalidate() {
  }

  @Override
  public void onAdViewCollapsed(TapSenseAdView adView) {
    if (mBannerListener != null) {
      mBannerListener.onBannerCollapsed();
    }
  }

  @Override
  public void onAdViewExpanded(TapSenseAdView adView) {
    if (mBannerListener != null) {
      mBannerListener.onBannerExpanded();
    }
  }

  @Override
  public void onAdViewFailedToLoad(TapSenseAdView adView, TSErrorCode errorCode) {
    if (mBannerListener != null) {
      mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
    }
  }

  @Override
  public void onAdViewLoaded(TapSenseAdView adView) {
    if (mBannerListener != null) {
      mBannerListener.onBannerLoaded(adView);
    }
  }

}
