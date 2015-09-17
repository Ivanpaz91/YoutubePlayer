package com.tirdis.watchorread.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Hashtable;

public class OpenSansTextView extends TextView {

	private Context context;

	public OpenSansTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public OpenSansTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public OpenSansTextView(Context context) {
		super(context);
		this.context = context;
	}

	public void setTypeface(Typeface tf, int style) {
		if (!isInEditMode()) {
			if (style == Typeface.NORMAL) {
				super.setTypeface(TypeFaceProvider.getTypeFace(getContext(), "fonts/OpenSans-Regular.ttf"));
			} else if (style == Typeface.ITALIC) {
				super.setTypeface(TypeFaceProvider.getTypeFace(getContext(), "fonts/OpenSans-Italic.ttf"));
			} else if (style == Typeface.BOLD) {
				super.setTypeface(TypeFaceProvider.getTypeFace(getContext(), "fonts/OpenSans-Bold.ttf"));
			} else if (style == Typeface.BOLD_ITALIC) {
				super.setTypeface(TypeFaceProvider.getTypeFace(getContext(), "fonts/OpenSans-BoldItalic.ttf"));
			}

		}
	}

	private static class TypeFaceProvider {

		private static Hashtable<String, Typeface> sTypeFaces = new Hashtable<String, Typeface>(
				4);

		public static Typeface getTypeFace(Context context, String fileName) {
			Typeface tempTypeface = sTypeFaces.get(fileName);

			if (tempTypeface == null) {
				tempTypeface = Typeface.createFromAsset(context.getAssets(), fileName);
				sTypeFaces.put(fileName, tempTypeface);
			}

			return tempTypeface;
		}
	}
}