//
//  ICTextFieldPadding.swift
//  iClem
//
//  Created by Odric Roux-Paris on 07/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

import Cocoa

class ICTextFieldPadding: NSTextFieldCell {
    let PADDING_MARGIN:CGFloat = 10.0
    
    
  
    
    override func titleRectForBounds(theRect: NSRect) -> NSRect {
        var titleFrame = super.titleRectForBounds(theRect)
        
        titleFrame.origin.x = PADDING_MARGIN
        
        titleFrame.size.width -= (2 * PADDING_MARGIN)
        
        return titleFrame
    }
    
    
    override func editWithFrame(aRect: NSRect, inView controlView: NSView, editor textObj: NSText, delegate anObject: AnyObject?, event theEvent: NSEvent) {
        var textFrame = aRect
        
        textFrame.origin.x += PADDING_MARGIN
        textFrame.size.width -= (2 * PADDING_MARGIN)
        
        super.editWithFrame(textFrame, inView: controlView, editor: textObj, delegate: anObject, event: theEvent)
    }
    
    override func selectWithFrame(aRect: NSRect, inView controlView: NSView, editor textObj: NSText, delegate anObject: AnyObject?, start selStart: Int, length selLength: Int) {
        var textFrame = aRect
        
        textFrame.origin.x += PADDING_MARGIN
        textFrame.size.width -= (2 * PADDING_MARGIN)
        super.selectWithFrame(textFrame, inView: controlView, editor: textObj, delegate: anObject, start: selStart, length: selLength)
    }
    
    override func drawInteriorWithFrame(cellFrame: NSRect, inView controlView: NSView) {
        var titleRect = self.titleRectForBounds(cellFrame)
        self.attributedStringValue.drawInRect(titleRect)
    }
    
    
}
