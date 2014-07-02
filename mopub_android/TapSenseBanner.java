package com.mopub.mobileads;

import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.tapsense.android.publisher.TSAdSize;
import com.tapsense.android.publisher.TSAdView;
import com.tapsense.android.publisher.TSAdViewListener;
import com.tapsense.android.publisher.TSErrorCode;
import com.tapsense.android.publisher.TapSenseAds;

public class TapSenseBanner extends CustomEventBanner implements TSAdViewListener{
  
  private CustomEventBannerListener mBannerListener;
  private TSAdView mAdBannerView;

  protected void loadBanner(Context context,
      CustomEventBannerListener customEventBannerListener,
      Map<String, Object> localExtras,
      Map<String, String> serverExtras){
    
    int width;
    int height;
    AdConfiguration adConfiguration = (AdConfiguration) localExtras.get("Ad-Configuration");

    if (adConfiguration != null) {
      width = adConfiguration.getWidth();
      height = adConfiguration.getHeight();
    } else if (serverExtras.containsKey("adWidth")
        && (serverExtras.containsKey("adHeight"))) {
      width = Integer.parseInt(serverExtras.get("adWidth"));
      height = Integer.parseInt(serverExtras.get("adHeight"));
    } else {
      mBannerListener
          .onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
      return;
    }

    mBannerListener = customEventBannerListener;

    if (!(context instanceof Activity)) {
      mBannerListener
          .onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
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

    TapSenseAds.disableGetNextAd();

    // Remove this before submitting to Play Store
    TapSenseAds.setTestMode();
    
    TapSenseAds.start(context, pubId, appId, secretKey);

    mAdBannerView = new TSAdView(context);
    mAdBannerView.setAdSize(new TSAdSize(width, height));
    mAdBannerView.setAdUnitId(adUnitId);
    mAdBannerView.setAdViewListener(this);
    mAdBannerView.loadAd();
  }

  @Override
  protected void onInvalidate() {
  }

  @Override
  public void onAdViewCollapsed(TSAdView adView) {
    if (mBannerListener != null) {
      mBannerListener.onBannerCollapsed();
    }
  }

  @Override
  public void onAdViewExpanded(TSAdView adView) {
    if (mBannerListener != null) {
      mBannerListener.onBannerExpanded();
    }
  }

  @Override
  public void onAdViewFailedToLoad(TSAdView adView, TSErrorCode errorCode) {
    if (mBannerListener != null) {
      mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
    }
  }

  @Override
  public void onAdViewLoaded(TSAdView adView) {
    if (mBannerListener != null) {
      mBannerListener.onBannerLoaded(adView);
    }
  }
  
}
