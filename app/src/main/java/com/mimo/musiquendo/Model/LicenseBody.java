package com.mimo.musiquendo.Model;


import android.content.Context;
import android.widget.TextView;

import com.mimo.musiquendo.R;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.ChildPosition;
import com.mindorks.placeholderview.annotations.expand.ParentPosition;

/**
 *
 */

@Layout(R.layout.license_item_body)
public class LicenseBody {

    @ParentPosition
    int mParentPosition;

    @ChildPosition
    int mChildPosition;

    @View(R.id.license_body)
    TextView bodyTxt;


    private Context context;
    private String mBody;

    public LicenseBody(Context context, String body) {
        this.context = context;
        this.mBody = body;
    }

    @Resolve
    public void onResolved() {
        bodyTxt.setText(mBody);
    }
}
