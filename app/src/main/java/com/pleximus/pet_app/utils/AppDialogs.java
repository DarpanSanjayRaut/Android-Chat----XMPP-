package com.pleximus.pet_app.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.pleximus.pet_app.R;

import java.util.List;

/**
 * This class contains all the Message Dialogs to be displayed to user
 * @author pleximus
 *
 */
public class AppDialogs {

	private final static int PADDING_LEFT_ERROR_LIST = 30;
	private final static int PADDING_RIGHT_ERROR_LIST = 20;
	private final static int PADDING_TOP_ERROR_LIST = 15;
	private final static int PADDING_BOTTOM_ERROR_LIST = 15;

	/**
	 * method to show custom error dialog
	 * @param context
	 * @param errors
	 */
	public static void showValidationErrorDialog(Context context, String title, List<String> errors) {
		try {
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_general);
			dialog.getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
			LinearLayout errorContainer = (LinearLayout) dialog.findViewById(R.id.container_error);
			TextView txtHeader = (TextView) dialog.findViewById(R.id.txt_dialog_header);
			txtHeader.setText(title);
			for(String error: errors) {
				LinearLayout linearLayout = new LinearLayout(context);
				linearLayout.setPadding(PADDING_LEFT_ERROR_LIST, PADDING_RIGHT_ERROR_LIST, PADDING_TOP_ERROR_LIST, PADDING_BOTTOM_ERROR_LIST);
				TextView txtError = new TextView(context);
				txtError.setGravity(Gravity.LEFT);
				txtError.setText(error);
				txtError.setTextColor(txtHeader.getTextColors().getDefaultColor());
				linearLayout.addView(txtError);
				errorContainer.addView(linearLayout);
			}
			dialog.show();

			Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);
			btnOK.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close dialog
                    dialog.dismiss();
                }
            });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
