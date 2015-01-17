//
//  ICWindow.swift
//  iClem
//
//  Created by Odric Roux-Paris on 17/01/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

import Cocoa


class ICWindow: NSWindow {
    var colorBackground:NSColor!;
    var viewTitleBar:ICViewColor!;
    
    override func awakeFromNib() {
        self.setBackgroundColor(NSColor(red: 0, green: 0.27, blue: 0.38, alpha: 1.00))
            }
    func setBackgroundColor(color:NSColor){
        //Accède à la view de titlebar
        let array = contentView.superview??.subviews
        
        for view in array!{
            if !view.isKindOfClass(NSClassFromString("NSTitlebarContainerView")){ continue};
            viewTitleBar = ICViewColor(frame: view.subviews[0].bounds);
            viewTitleBar.autoresizingMask = NSAutoresizingMaskOptions.ViewHeightSizable | NSAutoresizingMaskOptions.ViewWidthSizable
            viewTitleBar.colorBackground = color;
            
            view.subviews[0].addSubview(viewTitleBar, positioned: NSWindowOrderingMode.Below, relativeTo: view.subviews[0].subviews[0] as? NSView)
        }
    }

}

