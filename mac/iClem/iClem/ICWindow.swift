//
//  ICWindow.swift
//  iClem
//
//  Created by Odric Roux-Paris on 17/01/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

import Cocoa


class ICWindow: NSWindow {
    var colorTitle:NSColor {
        get {
            return self.colorTitle
        }
        set{
            //On recherche le textField
 
            let arraySubview: Array = subviewTitleBar() as [AnyObject]
            for view in arraySubview{
                if view.isKindOfClass(NSClassFromString("_NSThemeWidget")){
                    let textField: NSTextField = view as NSTextField
                    textField.textColor = newValue
                }
            }
                
            }
            
            
        }
    
    var colorBackground:NSColor {
        
        get{
            return self.colorBackground
        }
        set{
            var viewTitleBar:ICViewColor!;
            //Accède à la view de titlebar
            let array = contentView.superview??.subviews

            for view in array!{
                if !view.isKindOfClass(NSClassFromString("NSTitlebarContainerView")){ continue};
                viewTitleBar = ICViewColor(frame: view.subviews[0].bounds);
                viewTitleBar.autoresizingMask = NSAutoresizingMaskOptions.ViewHeightSizable | NSAutoresizingMaskOptions.ViewWidthSizable
                viewTitleBar.colorBackground = newValue;
                
                view.subviews[0].addSubview(viewTitleBar, positioned: NSWindowOrderingMode.Below, relativeTo: view.subviews[0].subviews[0] as? NSView)

        }
            
        
    }
    }
    
    
 
    
    override func awakeFromNib() {
        self.colorBackground = (NSColor(red: 0, green: 0.27, blue: 0.38, alpha: 1.00))
       // let subView = view.subviews as Array
        
       // println(subView[0].subviews)
        
        
        println(self.subviewTitleBar())
        
    }
    
    
    
    func subviewTitleBar() -> AnyObject{
        let array = contentView.superview??.subviews
        for view in array!{
            if !view.isKindOfClass(NSClassFromString("NSTitlebarContainerView")){continue}
            
            return view.subviews[0].subviews
        }
        
     return []
    }
    
    
}
    
    



