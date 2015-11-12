package com.yzh.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FlowTextView extends TextView {  
    
    public FlowTextView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    public FlowTextView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public FlowTextView(Context context) {  
        super(context);  
    }  
  
    @Override  
    public boolean isFocused() {  
        return true;  
    }  
  
}
