package com.ns.developer.tagview.widget;
/*
 * Copyright 2014 Namito.S
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.developer.tagview.R;
import com.ns.developer.tagview.entity.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * TagCloudLinkView Class
 * Simple Tagcloud Widget
 */
public class TagCloudLinkView extends RelativeLayout {

    /**
     * const
     */
    private static final int HEIGHT_WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private static final int TAG_LAYOUT_TOP_MERGIN = 10;
    private static final int TAG_LAYOUT_LEFT_MERGIN = 10;
    private static final int INNER_VIEW_PADDING = 10;
    private static final int DEFAULT_TEXT_SIZE = 14;
    private static final int DEFAULT_TAG_LAYOUT_COLOR = Color.parseColor("#aa66cc");
    private static final int DEFAULT_TAG_TEXT_COLOR = Color.parseColor("#1a1a1a");
    private static final int DEFAULT_DELETABLE_TEXT_COLOR = Color.parseColor("#1a1a1a");
    private static final String DEFAULT_DELETABLE_STRING = "×";

    /** tag list */
    private List<Tag> mTags = new ArrayList<Tag>();

    /**
     * System Service
     */
    private LayoutInflater mInflater;
    private Display mDisplay;
    private ViewTreeObserver mViewTreeObserber;

    /**
     * listener
     */
    private OnTagSelectListener mSelectListener;
    private OnTagDeleteListener mDeleteListener;

    /** view size param */
    private int mWidth;
    private int mHeight;

    /**
     * layout initialize flag
     */
    private boolean mInitialized = false;

    /**
     * custom layout param
     */
    private int mTagLayoutColor;
    private int mTagTextColor;
    private float mTagTextSize;
    private boolean mIsDeletable;
    private int mDeletableTextColor;
    private float mDeletableTextSize;

    /**
     * constructor
     * @param ctx
     * @param attrs
     */
    public TagCloudLinkView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        initialize(ctx, attrs, 0);
    }

    /**
     * constructor
     * @param ctx
     * @param attrs
     * @param defStyle
     */
    public TagCloudLinkView(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        initialize(ctx, attrs, defStyle);
    }

    /**
     * initalize instance
     * @param ctx
     * @param attrs
     * @param defStyle
     */
    private void initialize(Context ctx, AttributeSet attrs, int defStyle) {
        mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDisplay  = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        mViewTreeObserber = getViewTreeObserver();
        mViewTreeObserber.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(!mInitialized) {
                    mInitialized = true;
                    drawTags();
                }
            }
        });

        // get AttributeSet
        TypedArray typeArray = ctx.obtainStyledAttributes(attrs, R.styleable.TagCloudLinkView, defStyle, defStyle);
        mTagLayoutColor = typeArray.getColor(
                R.styleable.TagCloudLinkView_tagLayoutColor,DEFAULT_TAG_LAYOUT_COLOR);
        mTagTextColor = typeArray.getColor(
                R.styleable.TagCloudLinkView_tagTextColor,DEFAULT_TAG_TEXT_COLOR);
        mTagTextSize = typeArray.getDimension(
                R.styleable.TagCloudLinkView_tagTextSize,DEFAULT_TEXT_SIZE);
        mIsDeletable = typeArray.getBoolean(
                R.styleable.TagCloudLinkView_isDeletable, false);
        mDeletableTextColor = typeArray.getColor(
                R.styleable.TagCloudLinkView_deletableTextColor,DEFAULT_TAG_TEXT_COLOR);
        mDeletableTextSize = typeArray.getDimension(
                R.styleable.TagCloudLinkView_deletableTextSize,DEFAULT_DELETABLE_TEXT_COLOR);
    }

    /**
     * onSizeChanged
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
    }

    /**
     * view width
     * @return layout width
     */
    public int width() {
        return mWidth;
    }

    /**
     * view height
     * @return int layout height
     */
    public int height() {
        return mHeight;
    }

    /**
     * add Tag
     * @param tag
     */
    public void add(Tag tag) {
        mTags.add(tag);
    }

    /**
     * tag draw
     */
    public void drawTags() {

        if(!mInitialized) {
            return;
        }

        // clear all tag
        removeAllViews();

        // layout padding left & layout padding right
        float total = getPaddingLeft() + getPaddingRight();
        // 現在位置のindex
        int index = 1;
        // 相対位置起点
        int pindex = index;

        // List Index
        int listIndex = 0;
        for (Tag item : mTags) {
            final int position = listIndex;
            final Tag tag = item;

            // inflate tag layout
            View tagLayout = (View) mInflater.inflate(R.layout.tag, null);
            tagLayout.setId(index);
            tagLayout.setBackgroundColor(mTagLayoutColor);

            // tag text
            TextView tagView = (TextView) tagLayout.findViewById(R.id.tag_txt);
            tagView.setText(tag.getText());
            tagView.setPadding(INNER_VIEW_PADDING, INNER_VIEW_PADDING, INNER_VIEW_PADDING, INNER_VIEW_PADDING);
            tagView.setTextColor(mTagTextColor);
            tagView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTagTextSize);
            tagView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( mSelectListener != null) {
                        mSelectListener.onTagSelected(tag, position);
                    }
                }
            });

            // calculate　of tag layout width
            float tagWidth = tagView.getPaint().measureText(tag.getText())
                    + INNER_VIEW_PADDING * 2;  // tagView padding (left & right)

            // deletable text
            TextView deletableView = (TextView) tagLayout.findViewById(R.id.delete_txt);
            if(mIsDeletable) {
                deletableView.setVisibility(View.VISIBLE);
                deletableView.setText(DEFAULT_DELETABLE_STRING);
                deletableView.setPadding(INNER_VIEW_PADDING, INNER_VIEW_PADDING, INNER_VIEW_PADDING, INNER_VIEW_PADDING);
                deletableView.setTextColor(mDeletableTextColor);
                deletableView.setTextSize(mDeletableTextSize);
                deletableView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDeleteListener != null) {
                            Tag targetTag = tag;
                            TagCloudLinkView.this.remove(position);
                            mDeleteListener.onTagDeleted(targetTag, position);
                        }
                    }
                });
                tagWidth += deletableView.getPaint().measureText(DEFAULT_DELETABLE_STRING)
                        + INNER_VIEW_PADDING * 2; // deletableView Padding (left & right)
            } else {
                deletableView.setVisibility(View.GONE);
            }

            LayoutParams tagParams = new LayoutParams(HEIGHT_WC, HEIGHT_WC);
            tagParams.setMargins(0, 0, 0, 0);

            if (mWidth <= total + tagWidth) {
                tagParams.addRule(RelativeLayout.BELOW, pindex);
                tagParams.topMargin = TAG_LAYOUT_TOP_MERGIN;
                // initialize total param (layout padding left & layout padding right)
                total = getPaddingLeft() + getPaddingRight();
                pindex = index;
            } else {
                tagParams.addRule(RelativeLayout.ALIGN_TOP, pindex);
                tagParams.addRule(RelativeLayout.RIGHT_OF, index - 1);
                if (index > 1) {
                    tagParams.leftMargin = TAG_LAYOUT_LEFT_MERGIN;
                    total += TAG_LAYOUT_LEFT_MERGIN;
                }
            }
            total += tagWidth;
            addView(tagLayout, tagParams);
            index++;
            listIndex++;
        }

    }

    /**
     * get tag list
     * @return mTags TagObject List
     */
    public List<Tag> getTags() {
        return mTags;
    }

    /**
     * remove tag
     *
     * @param position
     */
    public void remove(int position) {
        mTags.remove(position);
        drawTags();
    }

    /**
     * setter for OnTagSelectListener
     * @param selectListener
     */
    public void setOnTagSelectListener(OnTagSelectListener selectListener) {
        mSelectListener = selectListener;
    }

    /**
     * setter for OnTagDeleteListener
     * @param deleteListener
     */
    public void setOnTagDeleteListener(OnTagDeleteListener deleteListener) {
        mDeleteListener = deleteListener;
    }

    /**
     * listener for tag select
     */
    public interface OnTagSelectListener {
        void onTagSelected(Tag tag, int position);
    }

    /**
     * listener for tag delete
     */
    public interface OnTagDeleteListener {
        void onTagDeleted(Tag tag, int position);
    }

}
