/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cherry.lib.doc.office.system.dialog;

import com.cherry.lib.doc.R;
import com.cherry.lib.doc.office.system.IControl;
import com.cherry.lib.doc.office.system.beans.CalloutView.CalloutManager;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ColorPickerDialog extends Dialog
{

    public static int mInitialColor;
    private Context mContext;
    private IControl control;
    private CalloutManager calloutMgr;

    public ColorPickerDialog(Context context, IControl control)
    {
        super(context);

        this.control = control;
        calloutMgr = control.getSysKit().getCalloutManager();
        mContext = context;
        mInitialColor = calloutMgr.getColor();

    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(mContext).inflate(R.layout.pen_setting_dialog, null);

        final ColorPickerView colorView = (ColorPickerView)view.findViewById(R.id.colorPickerView);
        final SeekBar strokeBar = (SeekBar)view.findViewById(R.id.strokeBar);
        final SeekBar alphaBar = (SeekBar)view.findViewById(R.id.alphaBar);
        Button okBtn = (Button)view.findViewById(R.id.ok);
        Button cancelBtn = (Button)view.findViewById(R.id.cancel);

        strokeBar.setProgress(calloutMgr.getWidth());
        strokeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {

            @ Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }

            @ Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @ Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (progress < 1)
                {
                    seekBar.setProgress(1);
                }
            }
        });

        alphaBar.setProgress(calloutMgr.getAlpha());
        colorView.setAlpha(calloutMgr.getAlpha());
        alphaBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {

            @ Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }

            @ Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @ Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                colorView.setAlpha(progress);
            }
        });

        // calloutBox.setChecked(mListener.isCalloutOn());
        // calloutBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        //
        // @Override
        // public void onCheckedChanged(CompoundButton buttonView,
        // boolean isChecked) {
        // if (isChecked) {
        // eraseBox.setChecked(false);
        // }
        // }
        // });
        //
        // eraseBox.setChecked(mListener.isEraseOn());
        // eraseBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        //
        // @Override
        // public void onCheckedChanged(CompoundButton buttonView,
        // boolean isChecked) {
        // if (isChecked) {
        // calloutBox.setChecked(false);
        // }
        // }
        // });

        okBtn.setOnClickListener(new View.OnClickListener()
        {

            @ Override
            public void onClick(View v)
            {
                // mListener.stateChanged(colorView.getColor(),
                // strokeBar.getProgress(), alphaBar.getProgress());
                calloutMgr.setColor(colorView.getColor());
                calloutMgr.setWidth(strokeBar.getProgress());
                calloutMgr.setAlpha(alphaBar.getProgress());
                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener()
        {

            @ Override
            public void onClick(View v)
            {
                dismiss();
            }
        });

        setContentView(view);
        setTitle(R.string.app_toolsbar_color);
    }
}
