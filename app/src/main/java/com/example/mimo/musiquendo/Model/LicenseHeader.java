package com.example.mimo.musiquendo.Model;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mimo.musiquendo.R;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.Collapse;
import com.mindorks.placeholderview.annotations.expand.Expand;
import com.mindorks.placeholderview.annotations.expand.Parent;
import com.mindorks.placeholderview.annotations.expand.ParentPosition;
import com.mindorks.placeholderview.annotations.expand.SingleTop;

/**
 * Clase modelo que almacena la cabecera de una licencia
 */

@Parent
@SingleTop
@Layout(R.layout.license_item_header)
public class LicenseHeader {

    @View(R.id.license_title)
    TextView headingTxt;

    @View(R.id.license_expand)
    ImageButton toggleIcon;

    @ParentPosition
    int mParentPosition;

    private Context mContext;
    private String mHeading;

    public LicenseHeader(Context context, String heading) {
        mContext = context;
        mHeading = heading;
    }

    @Resolve
    public void onResolved() {
        toggleIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_expand_more));
        headingTxt.setText(mHeading);
    }

    @Expand
    public void onExpand() {
        toggleIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_expand_less));
    }

    @Collapse
    public void onCollapse() {
        toggleIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_expand_more));
    }
}
