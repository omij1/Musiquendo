package com.mimo.musiquendo.Fragments;

import android.view.View;

import com.mimo.musiquendo.R;
import com.flipboard.bottomsheet.BaseViewTransformer;
import com.flipboard.bottomsheet.BottomSheetLayout;

/**
 * Clase que permite establecer el efecto de alejar el contenido cuando se muestra el bottomsheet
 * con las opciones de filtrado
 */

public class InsetViewTransformer extends BaseViewTransformer {

    @Override
    public void transformView(float translation, float maxTranslation, float peekedTranslation, BottomSheetLayout parent, View view) {
        float progress = Math.min(translation / peekedTranslation, 1);
        float scale = (1 - progress) + progress * 0.9f;
        view.setScaleX(scale);
        view.setScaleY(scale);

        if (translation == 0 || translation == parent.getHeight()) {
            parent.setBackgroundColor(0);
            ensureLayer(view, View.LAYER_TYPE_NONE);
        } else {
            parent.setBackgroundColor(view.getResources().getColor(R.color.backgroundColor));
            ensureLayer(view, View.LAYER_TYPE_HARDWARE);
        }

        float translationToTop = -(view.getHeight() * (1 - scale)) / 2;
        view.setTranslationY(translationToTop + progress * 20 * view.getContext().getResources().getDisplayMetrics().density);
    }


    private void ensureLayer(View view, int layerType) {
        if (view.getLayerType() != layerType) {
            view.setLayerType(layerType, null);
        }
    }
}