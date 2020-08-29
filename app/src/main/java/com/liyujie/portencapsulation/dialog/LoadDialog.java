package com.liyujie.portencapsulation.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liyujie.portencapsulation.R;


public class LoadDialog extends Dialog{

    public LoadDialog(@NonNull Context context) {
        super(context);
    }

    public LoadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_load);
    }
}
