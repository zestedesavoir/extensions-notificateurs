//
//  ICViewColor.swift
//  iClem
//
//  Created by Odric Roux-Paris on 17/01/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//  View qui change de couleur

import Cocoa

class ICViewColor: NSView {

    var colorBackground:NSColor!
    override func drawRect(dirtyRect: NSRect) {
        if let color = colorBackground{
            color.set();
            }
        NSRectFill(dirtyRect)
    }
    
}
