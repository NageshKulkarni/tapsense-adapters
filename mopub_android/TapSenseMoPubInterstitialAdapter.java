package com.mopub.mobileads;

import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.tapsense.android.publisher.TSErrorCode;
import com.tapsense.android.publisher.TSKeywordMap;
import com.tapsense.android.publisher.TapSenseAds;
import com.tapsense.android.publisher.TapSenseInterstitial;
import com.tapsense.android.publisher.TapSenseInterstitialListener;

public class TapSenseMoPubInterstitialAdapter extends CustomEventInterstitial
    implements TapSenseInterstitialListener {

  private CustomEventInterstitialListener mInterstitialListener;
  private TapSenseInterstitial mInterstitial;

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

    String adUnitId = serverExtras.containsKey("adUnitId") ? serverExtras
        .get("adUnitId") : "";

    // Remove this before submitting to Play Store
    TapSenseAds.setTestMode();

    mInterstitial = new TapSenseInterstitial(context, adUnitId, false,
        TSKeywordMap.EMPTY_TS_KEYWORD_MAP);
    mInterstitial.setListener(this);
    mInterstitial.requestAd();
  }

  @Override
  protected void showInterstitial() {
    if (mInterstitial.isReady()) {
      mInterstitial.showAd();
    }
  }

  @Override
  protected void onInvalidate() {
    mInterstitial.destroy();
  }

  @Override
  public void onInterstitialDismissed(TapSenseInterstitial interstitial) {
    if (mInterstitialListener != null) {
      mInterstitialListener.onInterstitialDismissed();
    }
  }

  @Override
  public void onInterstitialFailedToLoad(TapSenseInterstitial interstitial,
      TSErrorCode errorCode) {
    if (mInterstitialListener != null) {
      mInterstitialListener
          .onInterstitialFailed(MoPubErrorCode.NETWORK_NO_FILL);
    }
  }

  @Override
  public void onInterstitialLoaded(TapSenseInterstitial interstitial) {
    if (mInterstitialListener != null) {
      mInterstitialListener.onInterstitialLoaded();
    }
  }

  @Override
  public void onInterstitialShown(TapSenseInterstitial interstitial) {
    if (mInterstitialListener != null) {
      mInterstitialListener.onInterstitialShown();
    }
  }
}
