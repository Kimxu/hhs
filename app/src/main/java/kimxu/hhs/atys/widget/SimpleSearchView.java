package kimxu.hhs.atys.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.lang.reflect.Method;

import kimxu.hhs.R;
import kimxu.hhs.skin.SkinManager;

/**
 * 用于actionbar上的搜索框
 * 
 * @author hujingwei
 */
public class SimpleSearchView extends LinearLayout implements OnClickListener {
	private OnQueryTextListener mOnQueryChangeListener;
	private ImageButton mActionButton;
	private ImageButton mSubmitButton;
	private EditText mQueryTextView;
	private CharSequence mQueryHint;
	private ImageView mIconHint;
	private boolean mClearingFocus;
	private boolean enableClear;
	private CharSequence mOldQueryText;

	private int mWidthBefore;
	private int mWidth;
	private OnActionButtonClickListener mOnActionButtonClickListener;
	/*
	 * SearchView can be set expanded before the IME is ready to be shown during
	 * initial UI setup. The show operation is asynchronous to account for this.
	 */
	private Runnable mShowImeRunnable = new Runnable() {
		public void run() {

			mQueryTextView.requestFocus();

			InputMethodManager imm = (InputMethodManager) getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			if (imm != null) {
				showSoftInputUnchecked(SimpleSearchView.this, imm, 0);
			}
		}
	};

	public interface OnActionButtonClickListener {
		public void doAction();
	}

	/**
	 * Callbacks for changes to the query text.
	 */
	public interface OnQueryTextListener {

		/**
		 * Called when the user submits the query. This could be due to a key
		 * press on the keyboard or due to pressing a submit button. The
		 * listener can override the standard behavior by returning true to
		 * indicate that it has handled the submit request. Otherwise return
		 * false to let the SearchView handle the submission by launching any
		 * associated intent.
		 * 
		 * @param query
		 *            the query text that is to be submitted
		 * 
		 * @return true if the query has been handled by the listener, false to
		 *         let the SearchView perform the default action.
		 */
		boolean onQueryTextSubmit(String query);

		/**
		 * Called when the query text is changed by the user.
		 * 
		 * @param newText
		 *            the new content of the query text field.
		 * 
		 * @return false if the SearchView should perform the default action of
		 *         showing any suggestions if available, true if the action was
		 *         handled by the listener.
		 */
		boolean onQueryTextChange(String newText);
	}

	public interface OnQueryTextFocusChangeListener {
		boolean onFocused(boolean hasFocus, String query);
	}

	public SimpleSearchView(Context context) {
		super(context);
		initViews(context, null);
	}

	public SimpleSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context, attrs);
	}

	private void initViews(Context context, AttributeSet attrs) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.simple_search_view, this, true);

		mQueryTextView = (EditText) findViewById(R.id.search_text);
		mIconHint = (ImageView) findViewById(R.id.search_icon);
		mActionButton = (ImageButton) findViewById(R.id.search_action_btn);
		mSubmitButton = (ImageButton) findViewById(R.id.search_submit_btn);

		mActionButton.setOnClickListener(this);
		mSubmitButton.setOnClickListener(this);

		mQueryTextView.addTextChangedListener(mTextWatcher);
		mQueryTextView.setOnEditorActionListener(mOnEditorActionListener);

		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.SimpleSearchView, 0, 0);

			CharSequence queryHint = a
					.getText(R.styleable.SimpleSearchView_defaultQueryHint);
			if (!TextUtils.isEmpty(queryHint)) {
				setQueryHint(queryHint);
			}

			int imeOptions = a.getInt(
					R.styleable.SimpleSearchView_android_imeOptions, -1);
			if (imeOptions != -1) {
				setImeOptions(imeOptions);
			}
			int inputType = a.getInt(
					R.styleable.SimpleSearchView_android_inputType, -1);
			if (inputType != -1) {
				setInputType(inputType);
			}

			int actionButtonRes = a.getResourceId(
					R.styleable.SimpleSearchView_actionButtonSrc, -1);

			if (actionButtonRes != -1) {
				mActionButton.setImageResource(actionButtonRes);
			}

			int hintIconRes = a.getResourceId(
					R.styleable.SimpleSearchView_hintIconSrc, -1);

			if (hintIconRes != -1) {
				mIconHint.setImageResource(hintIconRes);
			}

			a.recycle();

			boolean focusable = true;

			a = context.obtainStyledAttributes(attrs, R.styleable.SherlockView,
					0, 0);
			focusable = a.getBoolean(
					R.styleable.SherlockView_android_focusable, focusable);
			a.recycle();
			setFocusable(focusable);
		}

		upateViews(null);
	}

	/**
	 * Sets the IME options on the query text field.
	 * 
	 * @see TextView#setImeOptions(int)
	 * @param imeOptions
	 *            the options to set on the query text field
	 * 
	 * @attr ref android.R.styleable#SearchView_imeOptions
	 */
	public void setImeOptions(int imeOptions) {
		mQueryTextView.setImeOptions(imeOptions);
	}

	/**
	 * Returns the IME options set on the query text field.
	 * 
	 * @return the ime options
	 * @see TextView#setImeOptions(int)
	 * 
	 * @attr ref android.R.styleable#SearchView_imeOptions
	 */
	public int getImeOptions() {
		return mQueryTextView.getImeOptions();
	}

	/**
	 * Sets the input type on the query text field.
	 * 
	 * @see TextView#setInputType(int)
	 * @param inputType
	 *            the input type to set on the query text field
	 * 
	 * @attr ref android.R.styleable#SearchView_inputType
	 */
	public void setInputType(int inputType) {
		mQueryTextView.setInputType(inputType);
	}

	/**
	 * Returns the input type set on the query text field.
	 * 
	 * @return the input type
	 * 
	 * @attr ref android.R.styleable#SearchView_inputType
	 */
	public int getInputType() {
		return mQueryTextView.getInputType();
	}

	/** @hide */
	@Override
	public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
		// Don't accept focus if in the middle of clearing focus
		if (mClearingFocus)
			return false;
		// Check if SearchView is focusable.
		if (!isFocusable())
			return false;

		return super.requestFocus(direction, previouslyFocusedRect);
	}

	/** @hide */
	@Override
	public void clearFocus() {
		mClearingFocus = true;
		setImeVisibility(false);
		super.clearFocus();
		mQueryTextView.clearFocus();
		mClearingFocus = false;
	}

	/**
	 * Sets a listener for user actions within the SearchView.
	 * 
	 * @param listener
	 *            the listener object that receives callbacks when the user
	 *            performs actions in the SearchView such as clicking on buttons
	 *            or typing a query.
	 */
	public void setOnQueryTextListener(OnQueryTextListener listener) {
		mOnQueryChangeListener = listener;
	}

	/**
	 * Sets a listener to inform when the focus of the query text field changes.
	 * 
	 * @param listener
	 *            the listener to inform of focus changes.
	 */
	public void setOnQueryTextFocusChangeListener(
			final OnQueryTextFocusChangeListener listener) {

		mQueryTextView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				// onTextFocusChanged(listener, hasFocus,
				// mQueryTextView.getEditableText());
				Editable query = mQueryTextView.getEditableText();
				if (query == null || mClearingFocus)
					return;
				listener.onFocused(hasFocus, query.toString());
			}

		});
	}

	/**
	 * Returns the query string currently in the text field.
	 * 
	 * @return the query string
	 */
	public CharSequence getQuery() {
		return mQueryTextView.getText();
	}

	/**
	 * Sets a query string in the text field and optionally submits the query as
	 * well.
	 * 
	 * @param query
	 *            the query string. This replaces any query text already present
	 *            in the text field.
	 * @param submit
	 *            whether to submit the query right now or only update the
	 *            contents of text field.
	 */
	public void setQuery(CharSequence query, boolean submit) {
		mQueryTextView.setText(query);
		if (query != null) {
			mQueryTextView.setSelection(mQueryTextView.length());
			// mUserQuery = query;
		}

		// If the query is not empty and submit is requested, submit the query
		if (submit && !TextUtils.isEmpty(query)) {
			submitQuery();
		}
	}

	/**
	 * Sets the hint text to display in the query text field. This overrides any
	 * hint specified in the SearchableInfo.
	 * 
	 * @param hint
	 *            the hint text to display
	 * 
	 * @attr ref android.R.styleable#SearchView_queryHint
	 */
	public void setQueryHint(CharSequence hint) {
		mQueryHint = hint;
		upateViews(null);
	}

	/**
	 * Gets the hint text to display in the query text field.
	 * 
	 * @return the query hint text, if specified, null otherwise.
	 * 
	 * @attr ref android.R.styleable#SearchView_queryHint
	 */
	public CharSequence getQueryHint() {
		if (mQueryHint != null) {
			return mQueryHint;
		}
		return null;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mWidth > 0) {
			int widthMode = MeasureSpec.EXACTLY;
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, widthMode);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void setWidth(int widthPixels) {
		mWidth = widthPixels;
		requestLayout();
	}

	private void setImeVisibility(final boolean visible) {
		if (visible) {
			post(mShowImeRunnable);
		} else {
			removeCallbacks(mShowImeRunnable);
			InputMethodManager imm = (InputMethodManager) getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			if (imm != null) {
				imm.hideSoftInputFromWindow(getWindowToken(), 0);
			}
		}
	}

	private void upateViews(String queryText) {
		if (queryText != null && queryText.length() > 0) {
			mIconHint.setVisibility(View.GONE);
			// mSubmitButton.setVisibility(View.VISIBLE);
		} else {
			if (mQueryHint != null) {
				mQueryTextView.setHint(" " + mQueryHint);
			} else {
				mQueryTextView.setHint(" ");
			}

			// mIconHint.setVisibility(View.VISIBLE);
			mSubmitButton.setVisibility(View.GONE);

		}

	}

	private final OnEditorActionListener mOnEditorActionListener = new OnEditorActionListener() {

		/**
		 * Called when the input method default action key is pressed.
		 */
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//			UMengConstant.addUMengLog(v.getContext(),
//					UMengConstant.EVENT_SEARCH_BUTTON_CLICK,
//					UMengConstant.SEARCH_CLICK_TYPE,
//					UMengConstant.SEARCH_SOFTINPUT_BUTTON);
			submitQuery();
			return true;
		}
	};

	private void onTextChanged(CharSequence newText) {
		if(enableClear){
			if("".equals(newText.toString().trim())){
				if(mActionButton.getVisibility() != View.GONE){
					mActionButton.setVisibility(View.GONE);
				}
			}else{
				if(mActionButton.getVisibility() != View.VISIBLE){
					mActionButton.setVisibility(View.VISIBLE);
				}
			}
		}
		
		if (mOnQueryChangeListener != null
				&& !TextUtils.equals(newText, mOldQueryText)) {
			mOnQueryChangeListener.onQueryTextChange(newText.toString());
		}
		mOldQueryText = newText.toString();

		upateViews(newText.toString());
	}

	public void submitQuery() {
		CharSequence query = mQueryTextView.getText();
		if (query != null && TextUtils.getTrimmedLength(query) > 0) {
			if (mOnQueryChangeListener != null) {
				setImeVisibility(false);
				mOnQueryChangeListener.onQueryTextSubmit(query.toString());
				clearFocus();
			}
		}
	}

	/**
	 * Callback to watch the text field for empty/non-empty
	 */
	private TextWatcher mTextWatcher = new TextWatcher() {

		public void beforeTextChanged(CharSequence s, int start, int before,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int after) {
			SimpleSearchView.this.onTextChanged(s);
		}

		public void afterTextChanged(Editable s) {
		}
	};

	private static void showSoftInputUnchecked(View view,
			InputMethodManager imm, int flags) {
		try {
			Method method = imm.getClass().getMethod("showSoftInputUnchecked",
					int.class, ResultReceiver.class);
			method.setAccessible(true);
			method.invoke(imm, flags, null);
		} catch (Exception e) {
			// Fallback to public API which hopefully does mostly the same thing
			imm.showSoftInput(view, flags);
		}
	}

	public void setActionButtonListener(OnActionButtonClickListener listener) {
		if (mActionButton != null) {
			mOnActionButtonClickListener = listener;
		}
	}

	public void setActionButtonEnabled(boolean enabled) {
		if (mActionButton != null) {
			// mActionButton.setVisibility(enabled ? VISIBLE : GONE);
		}
		// 屏蔽掉这个二维码按钮
		// mActionButton.setVisibility(View.GONE);
	}

	public void setSubmitButtonEnabled(boolean enabled) {
		if (mSubmitButton != null) {
			mSubmitButton.setVisibility(enabled ? VISIBLE : GONE);
		}
	}

	public void setQueryTextOnClickListener(OnClickListener listener) {
		mQueryTextView.setOnClickListener(listener);
	}

	public void setImeEnabled(boolean enable) {

		mQueryTextView.setInputType(enable ? InputType.TYPE_CLASS_TEXT
				: InputType.TYPE_NULL);
	}

	public void expand(final int targetWidth,
			AnimationListener animationListener) {

		mWidthBefore = getWidth();
		Animation anim = new Animation() {

			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				// TODO Auto-generated method stub

				int width = interpolatedTime == 1 ? targetWidth
						: (int) (mWidthBefore + (targetWidth - mWidthBefore)
								* interpolatedTime);

				mWidth = width;
				requestLayout();
			}

		};

		anim.setDuration((int) (targetWidth
				/ getContext().getResources().getDisplayMetrics().density / 2));
		anim.setAnimationListener(animationListener);
		startAnimation(anim);

	}

	public void collapse() {
		postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setWidth(mWidthBefore);
				mQueryTextView.setText("");
				clearFocus();
			}

		}, 800);

	}

	public void refresh() {
		mQueryTextView.setText("");
		clearFocus();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mSubmitButton) {
			submitQuery();
//			UMengConstant.addUMengLog(mSubmitButton.getContext(),
//					UMengConstant.EVENT_SEARCH_BUTTON_CLICK,
//					UMengConstant.SEARCH_CLICK_TYPE,
//					UMengConstant.SEARCH_BUTTON);
		} else if (v == mActionButton) {
			if(enableClear){
				mQueryTextView.setText(null);
			}
			if (mOnActionButtonClickListener != null) {
				mOnActionButtonClickListener.doAction();
			}
		}
	}

	public void showSoftInput() {
		postDelayed(mShowImeRunnable, 500);
	}

	public void setAllSelect() {
		if (mQueryTextView != null)
			mQueryTextView.setSelectAllOnFocus(true);
	}

	public void hideContent() {
		mActionButton.setVisibility(View.INVISIBLE);
		mSubmitButton.setVisibility(View.INVISIBLE);
		mQueryTextView.setVisibility(View.INVISIBLE);
		mIconHint.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * 开启清除输入内容的功能，此功能会在输入框最后显示一个清除图标，点击后会清除输入框中的内容
	 */
	public void enableClearIcon(){
		enableClear = true;
		if (SkinManager.isLight(getContext())) {
			mActionButton.setImageResource(R.drawable.abs__ic_clear_holo_light);
		} else {
			mActionButton.setImageResource(R.drawable.close_white);
		}
	}

	private static boolean canSubmitQuery = true;

	public void simple(View submitButton) {
		mActionButton.setVisibility(View.GONE);
		mSubmitButton.setVisibility(View.GONE);
		mQueryTextView.setVisibility(View.VISIBLE);
		mIconHint.setVisibility(View.GONE);
		findViewById(R.id.search_content_lay).setBackgroundResource(0);
		System.out.println("simple");
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (canSubmitQuery) {
					canSubmitQuery = false;
					submitQuery();
					new Handler().postDelayed(new Runnable() {
						// 设置0.5s后再次“可”提交查询，如果点击过快，会使搜索结果列表中有两组相同数据
						@Override
						public void run() {
							// TODO Auto-generated method stub
							canSubmitQuery = true;
						}

					}, 500);
				}
			}
		});
	}

	public EditText getEditor() {
		return mQueryTextView;
	}
}
