package com.wobi.android.wobiwriting.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;

/**
 * Created by wangyingren on 2017/11/30.
 */

public class SCSelectedDialog extends Dialog {
    public SCSelectedDialog(@NonNull Context context) {
        super(context);
    }

    public SCSelectedDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String title;
        private CustomDialog.MessageType type;
        private boolean flag;
        private View contentView;

        public Builder(Context context) {
            this.context = context;
        }

        public SCSelectedDialog.Builder setCancelable(boolean flag){
            this.flag = flag;
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public SCSelectedDialog.Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public SCSelectedDialog.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public SCSelectedDialog.Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
            final View layout = inflater.inflate(R.layout.dialog_sc_selected_layout, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setCancelable(flag);
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((FrameLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((FrameLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}

