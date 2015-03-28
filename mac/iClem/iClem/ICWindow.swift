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
            
            return textField()!.textColor!
        }
        
        set{
            //On recherche le textField
            if let field = textField(){
                println(field)
                //field.cell()?.colorTitle = newValue
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
    
    
    override func observeValueForKeyPath(keyPath: String, ofObject object: AnyObject, change: [NSObject : AnyObject], context: UnsafeMutablePointer<Void>) {
        let colorDefault = colorTitle
       self.colorTitle = colorDefault
        
    }

    
    override func awakeFromNib() {
        self.colorBackground = (NSColor(red: 0, green: 0.27, blue: 0.38, alpha: 1.00))
        self.colorTitle = NSColor.whiteColor()
       // let subView = view.subviews as Array
        
       // println(subView[0].subviews)
        //let field:NSTextField = textField()!
        //field.addObserver(self, forKeyPath: "textColor", options: NSKeyValueObservingOptions.New, context: nil)
        
        self.removeTextFieldTitle()
    }
    func removeTextFieldTitle() {
        let array = contentView.superview??.subviews
        for view in array!{
            if !view.isKindOfClass(NSClassFromString("NSTitlebarContainerView")){continue}
            
           var arrayView = view.subviews[0].subviews as [AnyObject]
            var i = 0;
            for object in arrayView{
                i += 1
                if !object.isKindOfClass(NSClassFromString("NSTextField")){continue}
                let field:NSTextField = textField()!
                arrayView.removeAtIndex(i-1)
                //view.subviews[0].subviews = arrayView as [AnyObject]
                
            }
        }
    

    }
    
    
    
    func subviewTitleBar() -> AnyObject{
        let array = contentView.superview??.subviews
        for view in array!{
            if !view.isKindOfClass(NSClassFromString("NSTitlebarContainerView")){continue}
            
            return view.subviews[0].subviews
        }
        
     return []
    }
    
    func textField() -> NSTextField?{
        
        let arraySubview: Array = subviewTitleBar() as [AnyObject]
        for view in arraySubview{
            if view.isKindOfClass(NSClassFromString("NSTextField")){
                let textField: NSTextField = view as NSTextField
               return textField
                
            }
        }
        
        return nil
        
    }
    
}





