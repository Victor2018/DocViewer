/*
 * 鏂囦欢鍚嶇О:          SlideShowSlideInfoAtom.java
 * 鐗堟潈鎵�鏈堾2001-2014 铏硅蒋锛堟澀宸烇級绉戞妧鏈夐檺鍏徃
 * 缂栬瘧鍣�:            android2.2
 * 鏃堕棿:              涓婂崍10:43:23
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

import java.util.Hashtable;

import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;

/**
 * TODO: An atom record that specifies what transition effect to perform 
 * during a slide show, and how to advance to the next presentation slide.
 * <p>
 * <p>
 * Read鐗堟湰:        Read V1.0
 * <p>
 * 浣滆��:            jqin
 * <p>
 * 鏃ユ湡:            2013-1-8
 * <p>
 * 璐熻矗浜�:           jqin
 * <p>
 * 璐熻矗灏忕粍:           
 * <p>
 * <p>
 */
public class SlideShowSlideInfoAtom extends PositionDependentRecordAtom
{
    private byte[] _header;
    private static long _type = 0x03F9;
    
    /**
     * an amount of time, in milliseconds, to wait before advancing to the 
     * next presentation slide. It MUST be greater than or equal to 0 
     * and less than or equal to 86399000. It MUST be ignored 
     * unless fAutoAdvance is TRUE
     */
    private int slideTime;
    
    /**
     * which sound to play when the transition starts.
     */
    private int soundIdRef;
    
    /**
     * the variant of effectType. See the effectType field for further restriction 
     * and specification of this field
     */
    private byte effectDirection;
    
    /**
     * which transition is used when transitioning to the next presentation slide 
     * during a slide show. Any of the following samples are for sample purposes only. 
     * Exact rendering of any transition is determined by the rendering application. 
     * As such, the same transition can have many variations depending on the implementation.
     * 0    Cut
     * 		The following specifies the possible effectDirection values and their meanings:
     *飩э��		0x00: The transition is not made through black. (The effect is the same as 
     *					no transition at all.)
     *飩э��		0x01: The transition is made through black.
     * 1    Random
     * 			effectDirection MUST be ignored.
     * 2    Blinds
     * 飩э��		0x00: Vertical
     *飩э��		0x01: Horizontal
     * 3    Checker
     * 飩э��		0x00: Horizontal
     * 飩э��		0x01: Vertical
     * 4    Cover
     * 飩э��		0x00: Left
     * 飩э��		0x01: Up
     * 飩э��		0x02: Right
     * 飩э��		0x03: Down
     * 飩э��		0x04: Left Up
     * 飩э��		0x05: Right Up
     * 飩э��		0x06: Left Down
     * 飩э��		0x07: Right Down
     * 5    Dissolve
     * 			effectDirection MUST be 0x00.
     * 6    Fade
     * 			effectDirection MUST be 0x00.
     * 7    Uncover
     * 飩э��		0x00: Left
     * 飩э�狅偋飥�	0x01: Up
     * 飩э�狅偋飥�	0x02: Right
     * 飩э�狅偋飥�	0x03: Down
     * 飩э�狅偋飥�	0x04: Left Up
     * 飩э�狅偋飥�	0x05: Right Up
     * 飩э�狅偋飥�	0x06: Left Down
     * 飩э�狅偋飥�	0x07: Right Down
     * 8    Random Bars
     * 飩э��		0x00: Horizontal
     * 			0x01: Vertica
     * 9    Strips
     * 飥�		0x04: Left Up
     * 飥�	飩э��0x05: Right Up
     * 飥�	飩э��0x06: Left Down
     * 飥�		0x07: Right Down
     * 10   Wipe
     *			0x00: Left
     *			0x01: Up
     *			0x02: Right
     *			0x03: Down
     * 11   Box In/Out
     * 飥�		0x00: Out
     * 飥狅偋飥�	0x01: In
     * 13   Split
     * 飥�		0x00: Horizontally out
     * 飥�	飩э��0x01: Horizontally in
     * 飥�	飩э��0x02: Vertically out
     * 飥�	飩э��0x03: Vertically in
     * 17   Diamond
     * 			effectDirection MUST be 0x00.
     * 18   Plus
     * 			effectDirection MUST be 0x00.
     * 19   Wedge
     * 			effectDirection MUST be 0x00.
     * 20   Push
     * 飥�		0x00: Left
     * 飥狅偋飥�	0x01: Up
     * 飥狅偋飥�	0x02: Right
     * 飥狅偋飥�	0x03: Down
     * 21   Comb
     * 			0x00: Horizontal
     * 		飩э��0x01: Vertical
     * 22   Newsflash
     * 			effectDirection MUST be 0x00.
     * 23   AlphaFade
     * 			effectDirection MUST be 0x00.
     * 26   Wheel
     * 			The value MUST be one of 0x01, 0x02, 0x03, 0x04, or 0x08.
     * 27   Circle
     * 			effectDirection MUST be 0x00.
     * 255  Undefined and MUST be ignored
     */
    private byte effectType;
    
    //whether the presentation slide can be manually advanced by the user during the slide show.
    private boolean fManualAdvance;
    
    //MUST be zero and MUST be ignored
    private boolean reserved1;
    
    //whether the corresponding slide is hidden and is not displayed during the slide show
    private boolean fHidden;
    
    //MUST be zero and MUST be ignored.
    private boolean reserved2;
    
    //whether to play the sound specified by soundIfRef.
    private boolean fSound;
    
    //MUST be zero and MUST be ignored.
    private boolean reserved3;
    
    //whether the sound specified by soundIdRef is looped continuously when playing until the next sound plays.
    private boolean fLoopSound;
    
    //MUST be zero and MUST be ignored.
    private boolean reserved4;
    
    //whether to stop any currently playing sound when the transition starts.
    private boolean fStopSound;
    
    //MUST be zero and MUST be ignored.
    private boolean reserved5;
    
    //whether the slide will automatically advance after slideTime milliseconds during the slide show.
    private boolean fAutoAdvance;
    
    //MUST be zero and MUST be ignored.
    private boolean reserved6;
    
    //whether to display the cursor during the slide show
    private boolean fCursorVisible;
    
    //MUST be zero and MUST be ignored.
    private byte reserved7;
    
    //how long the transition takes to run.
    //0x00    0.75 seconds
    //0x01    0.5 seconds
    //0x02    0.25 seconds
    private byte speed;
    
    private byte[] unused;
    
    /**
     * 
     */
    /**
     * For the UserEdit Atom
     */
    protected SlideShowSlideInfoAtom(byte[] source, int start, int len)
    {
        // Sanity Checking
        if(len < 24) { len = 24; }
        
        // Get the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);
        
        slideTime = LittleEndian.getInt(source, start + 8);
        soundIdRef = LittleEndian.getInt(source, start + 12);
        effectDirection = source[start + 16];
        effectType = source[start + 17];
        
        speed = source[start + 20];
    }
    
    public boolean isValidateTransition()
    {
    	switch(effectType)
    	{
	    	case 0:
	    		return effectDirection >= 0 && effectDirection <= 1;
	    	case 1:
	    		return true;
	    	case 2:
	    	case 3:
	    		return effectDirection >= 0 && effectDirection <= 1;
	    	case 4:
	    		return effectDirection >= 0 && effectDirection <= 7;
	    	case 5:
	    		return effectDirection == 0;
	    	case 6:
	    		return effectDirection == 0;
	    	case 7:
	    		return effectDirection >= 0 && effectDirection <= 7;
	    	case 8:
	    		return effectDirection >= 0 && effectDirection <= 1;
	    	case 9:
	    		return effectDirection >= 4 && effectDirection <= 7;
	    	case 10:
	    		return effectDirection >= 0 && effectDirection <= 3;
	    	case 11:
	    		return effectDirection >= 0 && effectDirection <= 1;
	    	case 13:
	    		return effectDirection >= 0 && effectDirection <= 3;
	    	case 17:
	    	case 18:
	    	case 19:
	    		return effectDirection == 0;
	    	case 20:
	    		return effectDirection >= 0 && effectDirection <= 3;
	    	case 21:
	    		return effectDirection >= 0 && effectDirection <= 1;
	    	case 22:
	    	case 23:
	    		return effectDirection == 0;
	    	case 26:
	    		return (effectDirection >= 1 && effectDirection <= 4) || (effectDirection == 8);
	    	case 27:
	    		return effectDirection == 0;
    	}
    	
    	return false;
    }
    
    /**
     * We are of type 12011
     */
    public long getRecordType()
    { 
        return _type; 
    }
    
    /**
     * At write-out time, update the references to PersistPtrs and
     *  other UserEditAtoms to point to their new positions
     */
    public void updateOtherRecordReferences(Hashtable<Integer,Integer> oldToNewReferencesLookup)
    {
       
    }
    
    /**
     * 
     */
    public void dispose()
    {
        _header = null;
    }
}
